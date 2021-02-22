import numpy as np


def fill_density_matrix(edges, n):
    res = np.zeros((n, n))

    # here I'll keep the degrees of all vertices
    vertex_degrees = np.zeros(n)
    cur_len = len(edges)
    for i in range(cur_len):
        vertex1 = edges[i][0]
        vertex2 = edges[i][1]
        res[vertex1][vertex2] = -1
        res[vertex2][vertex1] = -1
        vertex_degrees[vertex1] += 1
        vertex_degrees[vertex2] += 1

    for i in range(n):
        res[i][i] = vertex_degrees[i]

    return res


def find_cut_density(edges, n, current_v1):
    num_edges = 0
    current_v2 = []
    for i in range(n):
        if i not in current_v1:
            current_v2.append(i)

    for edge in edges:
        vertex1 = edge[0]
        vertex2 = edge[1]
        if (vertex1 in current_v1) and (vertex2 in current_v2):
            num_edges += 1

        if (vertex2 in current_v1) and (vertex1 in current_v2):
            num_edges += 1

    return n * num_edges / (len(current_v1) * len(current_v2))


def find_min_density_cut(edges, n):
    density_matrix = fill_density_matrix(edges, n)
    vectors = np.linalg.eigh(density_matrix)[1]

    # вектор, соответствующий второму по величине собственному значению
    # где 0 <= lambda_1 <= lambda_2 <= ...
    u_vector = vectors[:, 1]

    # упорядочиваем вершины графа по убываию их координат
    # в векторе u_vector
    coords = []
    for i in range(n):
        coords.append([i, u_vector[i]])

    coords = np.array(coords)
    sorted_vertices = np.argsort(coords[:, 1])
    sorted_vertices = sorted_vertices[::-1]

    cur_min_density = find_cut_density(edges, n, [sorted_vertices[0]])
    cur_answer = [sorted_vertices[0]]

    for cur_len in range(2, n - 1):
        cur_v1 = sorted_vertices[:cur_len]
        cur_density = find_cut_density(edges, n, cur_v1)
        if cur_density < cur_min_density:
            cur_min_density = cur_density
            cur_answer = np.copy(np.array(cur_v1))

    if len(cur_answer) <= n / 2:
        return cur_answer
    else:
        answer = []
        for i in range(n):
            if i not in cur_answer:
                answer.append(i)
        return np.array(answer)


def print_cut(res_cut):
    sorted_cut = np.sort(res_cut)
    answer = ""

    len_cut = len(sorted_cut)
    for i in range(len_cut):
        if i < len(sorted_cut - 1):
            answer = answer + str(sorted_cut[i]) + " "
        else:
            answer += str(sorted_cut[i])

    print(answer)


def main():
    n = int(input())

    # this variable will show the maximum vertex number
    max_num = 0
    all_edges = []

    for _ in range(n):
        vertex1, vertex2 = list(map(int, input().split(" ")))
        all_edges.append([vertex1, vertex2])
        if vertex1 > max_num:
            max_num = vertex1
        if vertex2 > max_num:
            max_num = vertex2

    res_cut = find_min_density_cut(all_edges, max_num + 1)
    print_cut(res_cut)


if __name__ == '__main__':
    main()
