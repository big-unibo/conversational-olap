import pandas as pd
import unittest

from modules import *


class TestAssess(unittest.TestCase):

    def test_all(self):
        X = pd.DataFrame([["a", 1, 2], ["b", 2, 2], ["c", 2, 1], ["d", 1, 1], ["e", 11, 10], ["f", 10, 11]])
        X.columns = ["foo", "quantity", "cost"]
        measures = X.columns[1:]

        outlier_detection(X, measures)
        self.assertTrue((X["anomaly"] >= -1).all())

        skyline(X, measures)
        self.assertTrue((X["dominance"] >= 0).all())

        clustering(X, measures)
        self.assertTrue((X["cluster_label"] >= 0).all())
        self.assertTrue((X["cluster_sil"].between(-1, 1)).all())

        self.assertTrue(len(X) == 6)

    def test_all2(self):
        X = pd.DataFrame([
            ["Beer", 35.0],
            ["Wine", 32.0],
            ["Cola", 30.0],
            ["Pizza", 6.0],
            ["Bread", 5.0]
        ])
        X.columns = ["foo", "quantity"]
        measures = X.columns[1:]

        outlier_detection(X, measures)
        self.assertTrue((X["anomaly"] >= -1).all())
        print(X)

        skyline(X, measures)
        self.assertTrue((X["dominance"] >= 0).all())

        clustering(X, measures)
        self.assertTrue((X["cluster_label"] >= 0).all())

        self.assertTrue(len(X) == 5)


if __name__ == '__main__':
    unittest.main()
