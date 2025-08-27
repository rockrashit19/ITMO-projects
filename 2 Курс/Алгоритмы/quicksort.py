def partition(a, l, r):
    pivot_index = l
    p = a[l]
    i = l
    j = r+1
    while True:
        while(a[i] < p):
            if i == r:
                break
            i+=1
        i+=1
        while(a[j] > p):
            j-=1
        if i >= j: break
        a[i], a[j] = a[j], a[i]
    return j

def quick_sort(a, l, r):
    if r <= l: return
    p = partition(a, l, r)
    quick_sort(a, l, p-1)
    quick_sort(a, p+1, r)
    
    return a

a = [3, 2, 7, 1, 10, 12, 4]

a = quick_sort(a, 0, len(a)-1)
print(a)

