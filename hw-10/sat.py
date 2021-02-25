from itertools import product
import numpy as np


def find_deg(n):
    cur_deg = 1
    while 1 == 1:
        if n <= cur_deg:
            return cur_deg
        else:
            cur_deg *= 2


def find_g_matrix(m):
    res = []
    n = 2 ** m
    cur_len = int(n / 2)
    res.append(np.ones(int(n)))
    for _ in range(int(m)):
        cur_res = []
        cur_times = int(n / cur_len / 2)
        for _ in range(cur_times):
            for _ in range(int(cur_len)):
                cur_res.append(0)
            for _ in range(int(cur_len)):
                cur_res.append(1)
        res.append(cur_res)
        cur_len /= 2
    return res


def do_xor(a, b):
    if a == b:
        return 0
    else:
        return 1


def find_all_codes(g_matrix, m):
    n = int(m + 1)
    res = []
    masks = [i for i in product([0, 1], repeat=1 * n)]
    y = len(g_matrix[0])

    for cur_mask in masks:
        cur_values = np.zeros(y)
        for j in range(n):
            for k in range(y):
                cur_values[k] = do_xor(cur_values[k], cur_mask[j] * g_matrix[j][k])
        res.append(cur_values)
    return res


def get_value(lin, vals):
    if vals[abs(int(lin[0])) - 1] == 0:
        if int(lin[0]) < 0:
            return 1
    else:
        if int(lin[0]) > 0:
            return 1
    if vals[abs(int(lin[1])) - 1] == 0:
        if int(lin[1]) < 0:
            return 1
    else:
        if int(lin[1]) > 0:
            return 1
    if vals[abs(int(lin[2])) - 1] == 0:
        if int(lin[2]) < 0:
            return 1
    else:
        if int(lin[2]) > 0:
            return 1
    return 0


def check_value(sat, vals, n, m):
    counter = 0
    for lin in sat:
        if get_value(lin, vals[:n]) == 1:
            counter += 1
    if counter / m >= 7 / 8:
        return 1
    else:
        return 0


def solve_sat(n, m, sat):
    pm_m = np.log2(find_deg(n))

    g_matrix = find_g_matrix(pm_m)

    pm_codes = find_all_codes(g_matrix, pm_m)

    if len(pm_codes[0]) > n:
        real_codes = np.array(pm_codes)[:, :n]
    else:
        real_codes = pm_codes

    for code in real_codes:
        counter = 0
        for lin in sat:
            if get_value(lin, code) == 1:
                counter += 1
        if counter / m >= 7 / 8:
            return code
    return np.zeros(n)


def print_result(res):
    res_str = ""
    for cur_res in res:
        res_str += str(int(cur_res))
    print(res_str)


def main():
    nums = input().split()
    n = int(nums[0])
    m = int(nums[1])
    sat = []
    for _ in range(m):
        line = input()
        sat.append(line.split())
    res = solve_sat(n, m, sat)
    print_result(res)


if __name__ == '__main__':
    main()
