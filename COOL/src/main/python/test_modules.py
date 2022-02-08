import pandas as pd
import unittest

from modules import *


class TestAssess(unittest.TestCase):
    B = pd.DataFrame([
        ["Beer", 35.0],
        ["Wine", 32.0],
        ["Cola", 30.0],
        ["Pizza", 6.0],
        ["Bread", 5.0]
    ], columns=["product", "quantity"])
    Bmea = ["quantity"]
    Battr = ["product"]

    C = pd.DataFrame([
        ["Beverages", "Beer", 35.0],
        ["Beverages", "Wine", 32.0],
        ["Beverages", "Cola", 30.0],
        ["Food", "Pizza", 6.0],
        ["Food", "Bread", 5.0]
    ], columns=["category", "product", "quantity"])
    Cmea = ["quantity"]
    Cattr = ["category"]

    def check(self, X, measures):
        outlier_detection(X, measures)
        self.assertTrue((X["anomaly"].between(-1, 1)).all())

        skyline(X, measures)
        self.assertTrue((X["dominance"].between(0, 1)).all())

        clustering(X, measures)
        self.assertTrue((X["cluster_label"] >= 0).all())
        self.assertTrue((X["cluster_sil"].between(-1, 1)).all())

    def test_all(self):
        X = pd.DataFrame([
            ["a", 1, 2],
            ["b", 2, 2],
            ["c", 2, 1],
            ["d", 1, 1],
            ["e", 11, 10],
            ["f", 10, 11]
        ])
        X.columns = ["foo", "quantity", "cost"]
        measures = X.columns[1:]

        self.check(X, measures)
        self.assertTrue(len(X) == 6)

    def test_all2(self):
        self.check(self.B, self.Bmea)

    def test_all3(self):
        X = intravariance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 2)

    def test_all4(self):
        X = univariance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 2)

    def test_all5(self):
        X = cardvariance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 5)

    def test_all6(self):
        X = maxratio(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 2)

if __name__ == '__main__':
    unittest.main()
