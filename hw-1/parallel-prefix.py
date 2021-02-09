import numpy as np


def print_or_gate_construction(gate_num, in_1, in_2):
    print("GATE ", gate_num, " OR ", in_1, " ", in_2)


def print_output(out_num, val_num):
    print("OUTPUT ", out_num, " ", val_num)


def make_scheme(n):
    # indexes 0..n-1 are for input values
    # so functional elements start with "n"
    cur_num = n

    # where the "edge" from the 1st element leads
    a_first_to = 1

    # here I keep the last element on each "vertical line" of the scheme
    cur_line_elemns = np.arange(0, n)

    # It's to update the previous array for the next iteration
    new_cur_line_elemns = np.arange(0, n)

    while a_first_to < n:
        for i in range(a_first_to, n):
            in_1 = cur_line_elemns[i - a_first_to]
            in_2 = cur_line_elemns[i]
            print_or_gate_construction(cur_num, in_1, in_2)
            new_cur_line_elemns[i] = cur_num
            cur_num += 1

        for i in range(a_first_to, n):
            cur_line_elemns[i] = new_cur_line_elemns[i]
        a_first_to *= 2

    # print outputs
    for j in range(n):
        print_output(j, cur_line_elemns[j])

    return 0


def main():
    n = int(input())
    make_scheme(n)

if __name__ == '__main__':
    main()

