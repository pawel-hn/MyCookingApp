package pawel.hn.mycookingapp

internal object Combination {
    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    private fun combinationUtil(
        arr: IntArray, data: IntArray, start: Int,
        end: Int, index: Int, r: Int
    ) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            for (j in 0 until r) {
                print(data[j].toString() + " ")
            }
            print("x")
            return
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            data[index] = arr[i]
            println("i: $i")
            combinationUtil(arr, data, i + 1, end, index + 1, r)
            i++
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    fun printCombination(arr: IntArray, n: Int, r: Int) {
        // A temporary array to store all combination one by one
        val data = IntArray(r)

        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r)
    }

    /*Driver function to check for above function*/
    @JvmStatic
    fun main(args: Array<String>) {
        val arr = intArrayOf(1, 2, 3, 4)
        val r = 3
        val n = arr.size
        printCombination(arr, n, r)
    }
}