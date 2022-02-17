import argparse
import math
import numpy as np
import pandas as pd
import sys
from sklearn.cluster import KMeans
from sklearn.ensemble import IsolationForest
from sklearn.metrics import silhouette_score, silhouette_samples


def clustering(X, measures):
    Z = X[measures].to_numpy()
    max_sil, best_k = -2, 1
    for k in range(2, min(6, math.ceil(len(X) / 2))):
        kmeans = KMeans(n_clusters=k, random_state=0).fit(Z)
        X["cluster_label_" + str(k)] = kmeans.labels_
        X["cluster_sil_" + str(k)] = silhouette_samples(Z, kmeans.labels_)
        silhouette_avg = silhouette_score(Z, kmeans.labels_)
        if silhouette_avg > max_sil:
            best_k = k
    X.drop(columns=[x for x in X.columns if ("cluster_label_" in x or "cluster_sil_" in x) and str(best_k) not in x], inplace=True)
    X.rename(columns={"cluster_label_" + str(best_k): "cluster_label", "cluster_sil_" + str(best_k): "cluster_sil"}, inplace=True)
    return X


def sadincrease(X, attributes, measures):
    X = X.fillna(0)
    for m in measures:
        X[m] = (X[m + ".x"] - X[m + ".y"]).abs() / (X[m + ".x"] + 1)
        X[m + "_kpi"] = (X[m] - X[m].mean()).abs()
    return X


def correlation(X, attributes, measures):
    def get_corrs(df):
        col_correlations = df[measures].corr()
        cor_pairs = col_correlations.stack()
        return [[k[0], k[1], v] for k, v in cor_pairs.to_dict().items() if k[0] < k[1]]

    return pd.DataFrame(get_corrs(X), columns=["m1", "m2", "correlation"])


def intravariance(X, attributes, measures):
    def v(x):
        A = pd.concat([(x.std() / (x.mean() + 1)).apply(lambda x: 1 if x > 1 else x) * 1.0, 1.0 * x.count() / len(X)],
                      axis=1)
        A.columns = ["intravariance", "cov"]
        return A.fillna(0)

    X = X.groupby(attributes)[measures].apply(lambda x: v(x)).reset_index().drop(columns=["level_1"])
    return X
    # return X[X["intravariance"] > 0.1]


def univariance(X, attributes, measures):
    def v(x):
        A = pd.concat(
            [1 - (x.std() / (x.mean() + 1)).apply(lambda x: 1 if x > 1 else x) * 1.0, 1.0 * x.count() / len(X)], axis=1)
        A.columns = ["univariance", "cov"]
        return A.fillna(0)

    X = X.groupby(attributes)[measures].apply(lambda x: v(x)).reset_index().drop(columns=["level_1"])
    return X
    # return X[X["univariance"] > 0.1]


def cardvariance(X, attributes, measures):
    X = X.copy(deep=True)
    y = X.groupby(attributes)[measures[0]].count()
    y = y.std() / y.mean()
    y = 1 if y > 1 else y
    X['cardvariance'] = y * 1.0
    X['cov'] = 1.0
    return X


# def maxratio(X, attributes, measures):
#     def v(x):
#         A = pd.concat([x.max() / x.sum() * 1.0, 1.0 * x.count() / len(X)], axis=1)
#         A.columns = ["maxratio", "cov"]
#         return A
#
#     return X.groupby(attributes)[measures].apply(lambda x: v(x))


def outlier_detection(X, measures):
    X["anomaly"] = IsolationForest(random_state=0).fit(X[measures]).decision_function(X[measures]) * -1.0
    X["anomaly"] = X["anomaly"].apply(lambda x: 0 if x < 0 else x)
    return X
    # return X[X["anomaly"] > 0.2]


def skyline(X, measures):
    # #########################################################################
    # https://stackoverflow.com/questions/32791911/fast-calculation-of-pareto-front-in-python
    # #########################################################################
    def is_pareto_efficient_simple(costs):
        """
        Find the pareto-efficient points
        :param costs: An (n_points, n_costs) array
        :return: A (n_points, ) boolean array, indicating whether each point is Pareto efficient
        """
        is_efficient = np.ones(costs.shape[0], dtype=bool)
        for i, c in enumerate(costs):
            if is_efficient[i]:
                is_efficient[is_efficient] = np.any(costs[is_efficient] >= c,
                                                    axis=1)  # Keep any point with a lower cost
                is_efficient[i] = True  # And keep self
        return is_efficient

    X["dominance"] = is_pareto_efficient_simple(X[measures].to_numpy())
    X["score"] = 0
    for m in measures:
        max = X[m].max()
        X[m + "_norm"] = X[m] / max
        X["score"] = X.apply(lambda x: x["score"] + x[m + "_norm"] if x["dominance"] else 0, axis=1)
    X["dominance"] = X["score"] * 1.0 / len(measures)
    X.drop(["score"] + [m + "_norm" for m in measures], axis=1, inplace=True)
    return X


if __name__ == '__main__':
    ###############################################################################
    # PARAMETERS SETUP
    ###############################################################################
    toprint = {}
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", type=str)
    parser.add_argument("--module", type=str)
    parser.add_argument("--measures", type=str)
    parser.add_argument("--attributes", type=str)
    parser.add_argument("--path", type=str)
    args = parser.parse_args()
    module = args.module
    # measures = args.measures.lower().split(",")
    # attributes = args.attributes.lower().split(",")
    measures = args.measures.split(",")
    attributes = args.attributes.split(",") if args.attributes is not None else []
    df = pd.DataFrame([])
    try:
        df = pd.read_csv(args.path + args.file, encoding='utf-8')
    except:
        df = pd.read_csv(args.path + args.file, encoding='cp1252')
    df.dropna(inplace=True)

    # df.columns = [x.lower() for x in df.columns]
    if module == "outlierdetection":
        df = outlier_detection(df, measures)
    elif module == "skyline":
        df = skyline(df, measures)
    elif module == "clustering":
        df = clustering(df, measures)
    elif module == "intravariance":
        df = intravariance(df, attributes, measures)
    elif module == "univariance":
        df = univariance(df, attributes, measures)
    elif module == "cardvariance":
        df = cardvariance(df, attributes, measures)
    elif module == "correlation":
        df = correlation(df, attributes, measures)
    elif module == "sadincrease":
        df = sadincrease(df, attributes, measures)
    else:
        print("Unknown module: " + module)
        sys.exit(1)
    df.to_csv(args.path + args.file, index=False)
