import pandas as pd
import numpy as np
import unittest
import time
from modules import *


class TestAssess(unittest.TestCase):
    A = pd.DataFrame([
        ["Beer", 35.0, 70.0],
        ["Wine", 32.0, 40.0],
        ["Cola", 30.0, 55.0],
        ["Pizza", 6.0, 12.0],
        ["Bread", 5.0, 5.0]
    ], columns=["product", "quantity", "revenue"])
    Amea = ["quantity", "revenue"]
    Aattr = ["product"]

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

    D = pd.DataFrame([
        ["Beer", 35.0, 23.0],
        ["Wine", 32.0, 15.0],
        ["Cola", 30.0, 8.0],
        ["Pizza", 6.0, 0.0],
        ["Bread", 5.0, 1.0]
    ], columns=["product", "quantity.x", "quantity.y"])
    Dmea = ["quantity"]
    Dattr = ["product"]

    def check(self, X, measures):
        outlier_detection(X, measures)
        self.assertTrue((X["anomaly"].between(-1, 1)).all())

        skyline(X, measures)
        self.assertTrue((X["dominance"].between(0, 1)).all())

        clustering(X, measures, "foo_filename")
        self.assertTrue((X["cluster_label"] >= 0).all())
        self.assertTrue((X["cluster_sil"].between(-1, 1)).all())

        X = pd.DataFrame(np.random.randint(0, 100, size=(100, 3)), columns=list('ABC'))
        start = time.time()
        clustering(X, ["C"], "foo_filename")
        self.assertTrue((X["cluster_label"] >= 0).all())
        self.assertTrue((X["cluster_sil"].between(-1, 1)).all())
        end = time.time()

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
        X = aggregation_variance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 2)
        self.assertTrue((X["cov"] <= 1).all() and (X["cov"] > 0).all())

    def test_all4(self):
        X = uniform_aggregation_variance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 2)
        self.assertTrue((X["cov"] <= 1).all() and (X["cov"] > 0).all())

    def test_all5(self):
        X = domain_variance(self.C, self.Cattr, self.Cmea)
        self.assertTrue(len(X) == 5)

    def test_all7(self):
        X = correlation(self.A, self.Aattr, self.Amea)
        self.assertTrue(len(X) == 1)

    def test_all8(self):
        X = slicing_variance(self.A, self.Aattr, self.Amea)
        self.assertTrue(len(X) == 1)


if __name__ == '__main__':
    unittest.main()
