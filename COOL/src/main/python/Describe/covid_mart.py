# -*- coding: utf-8 -*-
import pandas as pd
from sqlalchemy import create_engine
import datetime
from datetime import date

engine = create_engine("mysql+pymysql://{user}:{pw}@137.204.74.2:3307/{db}".format(user="mfrancia", pw="mfrancia", db="covid_mart"))

df = pd.read_csv("covid.csv")
df = df[df['countriesAndTerritories'].notnull() & df['geoId'].notnull() & df['popData2018'].notnull()]
df["dateRep"] = df["dateRep"].apply(lambda x: datetime.datetime.strptime(x, '%m/%d/%Y'))
df["month"]   = df["dateRep"].apply(lambda x: x.strftime('%Y/%m'))
df["dateRep"] = df["dateRep"].apply(lambda x: x.strftime('%Y/%m/%d')) # %d/%m/%Y
print(df["dateRep"].max())

country = df[["geoId", "countriesAndTerritories", "countryterritoryCode", "popData2018", "continentExp"]]
country = country.drop_duplicates()

date = df[["dateRep", "month", "year"]]
date = date.drop_duplicates()

df["cases100K"]  = df["cases"]  / (df["popData2018"] /  100000)
df["cases1M"]    = df["cases"]  / (df["popData2018"] / 1000000)
df["deaths100K"] = df["deaths"] / (df["popData2018"] /  100000)
df["deaths1M"]   = df["deaths"] / (df["popData2018"] / 1000000)
df["deaths14"]   = df.apply(lambda x: x["deaths"] / (x["popData2018"] / 100000) if x["dateRep"] >= "2020/05/04" else 0, axis=1)
df["cases14"]    = df.apply(lambda x: x["cases"]  / (x["popData2018"] / 100000) if x["dateRep"] >= "2020/05/04" else 0, axis=1)

# def sumLast14days(x, column):
#     x = x.loc[x["dateRep"] >= "2020/05/04"]
#     c = x[column].mean()
#     return c
# dff = df.groupby(["countriesAndTerritories"]).apply(lambda x: sumLast14days(x, "cases"))
# dff = df.groupby(["countriesAndTerritories"]).apply(lambda x: sumLast14days(x, "deaths"))

fact = df[["dateRep", "countriesAndTerritories", 
           "cases", "cases100K", "cases1M", "cases14", 
           "deaths", "deaths100K", "deaths1M", "deaths14"]]
fact = fact.drop_duplicates()

country.to_sql(con=engine,  name='country', if_exists='append', index=False)
date.to_sql(con=engine,     name='date',    if_exists='append', index=False)
fact.to_sql(con=engine,     name='fact',    if_exists='append', index=False)