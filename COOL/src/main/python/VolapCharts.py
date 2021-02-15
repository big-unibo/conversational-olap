import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from textwrap import wrap

path = "../../../outputs/Volap 2021-02-11_18-10-02.csv"
results = pd.read_csv(path, header=None).to_numpy()

nColumns = 3
nRows = math.ceil(len(results) / nColumns)
fig = plt.figure(figsize=(4.5 * nColumns, 6 * nRows))
plt.subplots_adjust(left=0.05, bottom=0.10, right=0.95, top=0.90, wspace=0.15, hspace=0.40)

for i in range(len(results)):
    ax = fig.add_subplot(nRows, nColumns, i + 1)
    ax.grid(True)
    ax.set_title("\n".join(wrap(results[i][0], 45)))
    ax.set_xlabel("Average Relative Error")
    ax.set_ylabel("Quality")
    ax.set_xlim([0, 0.5])
    ax.set_ylim([0, 0.5])
    if len(results[i]) > 5 and ~np.isnan(results[i][5]) and ~np.isnan(results[i][6]):
        ax.plot(results[i][5], results[i][6], color="green", label="Original", ls="", marker="s", markersize=13)
    if len(results[i]) > 5 and np.isnan(results[i][5]) and ~np.isnan(results[i][6]):
        ax.axhline(y=results[i][6], color='green', label="Original", linewidth=4)
    if len(results[i]) > 3 and ~np.isnan(results[i][3]) and ~np.isnan(results[i][4]):
        ax.plot(results[i][3], results[i][4], color="blue", label="Base", ls="", marker="D", markersize=11)
    if len(results[i]) > 1 and ~np.isnan(results[i][1]) and ~np.isnan(results[i][2]):
        ax.plot(results[i][1], results[i][2], color="red", label="Complete", ls="", marker="o", markersize=14)
    ax.legend()

plt.savefig(path.replace(".csv", ".pdf"))
