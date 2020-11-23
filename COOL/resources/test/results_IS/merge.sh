#!/bin/bash
# set -e
rm test_merged.csv
cp test_dataset_patrick_bktree.csv                     test_merged.csv
tail -n +2 test_dataset_patrick_sequential.csv >>      test_merged.csv
tail -n +2 test_dataset_patrick_ssb_bktree.csv >>      test_merged.csv
tail -n +2 test_dataset_patrick_ssb_sequential.csv >>  test_merged.csv