import pandas as pd 
from sklearn.cluster import KMeans
from sklearn.ensemble import IsolationForest
from sklearn.metrics import silhouette_samples, silhouette_score
from yellowbrick.cluster import KElbowVisualizer
from scipy import stats
import numpy as np
import argparse

###############################################################################
# PARAMETER SETUP
###############################################################################
parser = argparse.ArgumentParser()
parser.add_argument("--path", help="where to put the output", type=str)
parser.add_argument("--file", help="the file name", type=str)
parser.add_argument("--session_step", help="the session step", type=int)
parser.add_argument("--k", help="size k", type=int)
parser.add_argument("--models", nargs='*', help="mining models to apply")
args  = parser.parse_args()
debug = args.path is None
path  =         "../../../resources/output/" if debug else args.path
file  =         "test4"                      if debug else args.file
session_step = 0                             if debug else args.session_step
k =                                                        args.k
models = ["top-k", "bottom-k", "clustering", "outliers", "skyline"]
if args.models is not None:
    models = args.models

###############################################################################
# APPLY MODELS
###############################################################################
if not debug:
    X = pd.read_csv(path + file + "_" + str(session_step) + ".csv")
else:
    df = [
            ["city", "sum(unitsales)"], 
            ["CE",                150],
            ["BO",                 75],
            ["MI",               1000],
         ]

    df = [
            ["region", "sum(unitsales)"], 
            ["ER",                  225],
            ["LO",                 1000],
         ]

    df = [
            ["region", "product", "sum(unitsales)"], 
            ["ER",     "COCA",                 100],
            ["ER",     "FANTA",                125],
            ["LO",     "PIZZA",               1000]
         ]
    
    df = [
            ["region", "product", "sum(unitsales)"], 
            ["ER",     "COCA",                 100],
         ]

    df = [
            ["region", "city", "product", "sum(unitsales)", "sum(storesales)"], 
            ["ER"    , "CE",   "COCA",                 100,               100],
            ["ER"    , "CE",   "FANTA",                 50,                50],
            ["ER"    , "BO",   "FANTA",                 75,                75],
            ["LO"    , "MI",   "PIZZA",               1000,              1000],            
         ]

    X = pd.DataFrame(df[1:], columns=df[0], dtype = float)

measures = [x for x in X.columns if "(" in x]
P = pd.DataFrame(columns=["model", "component", "property", "value"])

if len(X.index) > 0:
    prop = []
    if "clustering" in models and (k is None or k > 1):
        def_k = k

        if (len(X.index) > 4):
            if (def_k is None):
                model = KMeans()
                visualizer = KElbowVisualizer(model, k=(1, min(6, len(X.index))))
                visualizer.fit(X[measures]) # Fit the data to the visualizer
                def_k = visualizer.elbow_value_
    
            if (def_k is None):
                def_k = 3
    
            if (def_k < len(X.index)):
                kmeans = KMeans(n_clusters=def_k, random_state=0).fit(X[measures])
                X["model_clustering"] = kmeans.labels_
                # print(kmeans.inertia_)
                for idx, c in enumerate(kmeans.cluster_centers_):
                    prop.append(["model_clustering", idx, "centroid", round(c[0], 2)])

    
    if "outliers" in models:
        def_k = k
        if (def_k is None):
            def_k = int(len(X.index) / 4)
        outliers = IsolationForest(random_state=0).fit(X[measures])
        X["outlierness"] = outliers.predict(X[measures])
        X["model_outliers"] = X["outlierness"].isin(X[X["outlierness"] < 0]["outlierness"].nsmallest(def_k, keep='first'))
        prop.append(["model_outliers", "True",  "outlierness", round(X[X["model_outliers"] == True]["outlierness"].mean(), 2)])
        prop.append(["model_outliers", "False", "outlierness", round(X[X["model_outliers"] == False]["outlierness"].mean(), 2)])

    if "skyline" in models and len(measures) > 1:
        # #########################################################################
        # https://stackoverflow.com/questions/32791911/fast-calculation-of-pareto-front-in-python
        # #########################################################################
        def is_pareto_efficient_simple(costs):
            """
            Find the pareto-efficient points
            :param costs: An (n_points, n_costs) array
            :return: A (n_points, ) boolean array, indicating whether each point is Pareto efficient
            """
            is_efficient = np.ones(costs.shape[0], dtype = bool)
            for i, c in enumerate(costs):
                if is_efficient[i]:
                    is_efficient[is_efficient] = np.any(costs[is_efficient]>=c, axis=1)  # Keep any point with a lower cost
                    is_efficient[i] = True  # And keep self
            return is_efficient
        X["model_skyline"] = is_pareto_efficient_simple(X[measures].to_numpy())
    
    for m in measures:
        X["zscore_" + m] = stats.zscore(X[m])
        X["zscore_" + m] = round(X["zscore_" + m].fillna(0), 3)
    
    if "top-k" in models:
        def_k = k
        if (def_k is None):
            def_k = int(len(X.index) / 4)
        
        for m in measures:
            X["model_top_" + m] = X[m].isin(X[m].nlargest(def_k, keep='first'))
            prop.append(["model_top_" + m, "True",  "avgZscore", round(X[X["model_top_" + m] == True]["zscore_" + m].mean(), 2)])
            prop.append(["model_top_" + m, "False", "avgZscore", round(X[X["model_top_" + m] == False]["zscore_" + m].mean(), 2)])

    if "bottom-k" in models:
        def_k = k
        if (def_k is None):
            def_k = int(len(X.index) / 4)
        for m in measures:
            X["model_bottom_" + m] = X[m].isin(X[m].nsmallest(def_k, keep='first'))
            prop.append(["model_bottom_" + m, "True",  "avgZscore", round(X[X["model_bottom_" + m] == True]["zscore_" + m].mean(), 2)])
            prop.append(["model_bottom_" + m, "False", "avgZscore", round(X[X["model_bottom_" + m] == False]["zscore_" + m].mean(), 2)])

    P = P.append(pd.DataFrame(prop, columns=["model", "component", "property", "value"]))
    
    if not debug:
        X.to_csv(path + file + "_" + str(session_step) + "_ext.csv", index=False)
        P.to_csv(path + file + "_" + str(session_step) + "_properties.csv", index=False)
    else:
        print(X)
        print(P)
else:
    raise ValueError('Empty data')