def insertion_sort(a, n):
    for i in range(1, n):
        t = a[i]
        j = i
        while j > 0 and a[j-1] > t:
            a[j] = a[j-1]
            j-=1
        a[j] = t
    return a

def bubble_sort(a, n):
    swap = True
    while (swap):
        swap = False
        n -= 1
        for i in range(n):
            if(a[i+1]<a[i]):
                a[i], a[i+1] = a[i+1], a[i]
                swap = True
    return a

def selection_sort(a, n):
    for i in range(n-1):
        for j in range(i+1, n):
            if (a[j] < a[i]):
                a[j], a[i] = a[i], a[j]
        print(a)
    return a

a = [3, 5, 6, 4, 10, 1]

print(selection_sort(a, len(a)))
# a = insertion_sort(a, len(a))
# a = bubble_sort(a, len(a))
# print(a)