import argparse
import numpy as np
import pandas as pd
import sys
from sklearn.ensemble import IsolationForest

import os
print("path: " + os.path.abspath(os.getcwd()))

def outlier_detection(X, measures):
    X["kpi"] = IsolationForest(random_state=0).fit(X[measures]).predict(X[measures])
    return X


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

    X["kpi"] = is_pareto_efficient_simple(X[measures].to_numpy())
    X["score"] = 0
    for m in measures:
        max = X[m].max()
        X[m + "_norm"] = X[m] / max
        X["score"] = X.apply(lambda x: x["score"] + x[m + "_norm"] if x["kpi"] else -1, axis=1)
    X["kpi"] = X["score"] / len(measures)
    return X.drop(["score"] + [m + "_norm" for m in measures], axis=1)


if __name__ == '__main__':
    ###############################################################################
    # PARAMETERS SETUP
    ###############################################################################
    toprint = {}
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", type=str)
    parser.add_argument("--module", type=str)
    parser.add_argument("--measures", type=str)
    parser.add_argument("--path", type=str)
    args = parser.parse_args()
    module = args.module
    measures = args.measures.split(",")

    df = pd.read_csv(args.path + args.file)
    if module == "outlierdetection":
        df = outlier_detection(df, measures)
    elif module == "skyline":
        df = skyline(df, measures)
    else:
        print("Unknown module: " + module)
        sys.exit(1)
    df.to_csv(args.path + args.file, index=False)
