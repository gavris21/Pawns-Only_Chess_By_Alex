package chess

import java.lang.StrictMath.abs

class Chess (player1: String, player2: String) {
    var state = State.ACTION_PLAYER1
    private val name1 = player1
    private val name2 = player2
    private val log = MutableList(1) { Array(4) { 0 } }
    private val list = MutableList(8) { MutableList(8) {' '} }

    init {
        list[1] = MutableList(8) {'W'}
        list[6] = MutableList(8) {'B'}
    }

    fun drawInitialBoard(){
        for (i in 7 downTo 0) {
            print("  +")
            repeat(8) { print("---+")}
            print("\n${i+1} |")
            for (j in 0..7) print(" ${list[i][j]} |")
            println()
        }
        print("  +")
        repeat(8) { print("---+")}
        print("\n ")
        for (k in 'a'..'h') {
            print("   $k")
        }
        println("\n")
    }

    private fun checkInput(input: String): Boolean {
        if (input.matches("[a-h][1-8][a-h][1-8]".toRegex())) return true
        println("Invalid Input")
        return false
    }

    private fun parse(input: String): Array<Int> {
        val arr = Array(4) { 0 }
        arr[1] = input[1].digitToInt() - 1
        arr[3] = input[3].digitToInt() - 1
        arr[0] = when (input[0]) {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else -> 0
        }
        arr[2] = when (input[2]) {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else -> 0
        }
        return arr
    }

    private fun checkMove(input: String): Boolean {
        val arr = parse(input)
        when(state) {
            State.ACTION_PLAYER1 -> {
                if (list[arr[1]][arr[0]] != 'W') {
                    println("No white pawn at ${input[0]}${input[1]}")
                    return false
                }
                if (arr[1] != 1 && arr[3] - arr[1] != 1) {
                    println("Invalid Input")
                    return false
                }
                if (arr[1] == 1 && arr[3] - arr[1] != 1 && arr[3] - arr[1] != 2) {
                    println("Invalid Input")
                    return false
                }
                // Capture
                if (abs(arr[2] - arr[0]) == 1 && list[arr[3]][arr[2]] == 'B') {
                    return true
                }
                // En passant
                if (abs(arr[2] - arr[0]) == 1 &&
                    list[arr[3] - 1][arr[2]] == 'B' &&
                    log[log.lastIndex][2] == arr[2] && log[log.lastIndex][3] == arr[3] - 1 &&
                    log[log.lastIndex][1] - log[log.lastIndex][3] == 2) {
                    list[arr[3] - 1][arr[2]] = ' '
                    return true
                }


                if (arr[0] != arr[2]) {
                    println("Invalid Input")
                    return false
                }
                if (list[arr[3]][arr[2]] == 'B') {
                    println("Invalid Input")
                    return false
                }
            }
            State.ACTION_PLAYER2 -> {
                if (list[arr[1]][arr[0]] != 'B') {
                    println("No black pawn at ${input[0]}${input[1]}")
                    return false
                }
                if (arr[1] != 6 && arr[1] - arr[3] != 1) {
                    println("Invalid Input")
                    return false
                }
                if (arr[1] == 6 && arr[1] - arr[3] != 1 && arr[1] - arr[3] != 2) {
                    println("Invalid Input")
                    return false
                }
                // Capture
                if (abs(arr[2] - arr[0]) == 1 && list[arr[3]][arr[2]] == 'W') {
                    return true
                }
                // En passant
                if (abs(arr[2] - arr[0]) == 1 &&
                    list[arr[3] + 1][arr[2]] == 'W' &&
                    log[log.lastIndex][2] == arr[2] &&
                    log[log.lastIndex][3] == arr[3] + 1 &&
                    log[log.lastIndex][3] - log[log.lastIndex][1] == 2) {
                    list[arr[3] + 1][arr[2]] = ' '
                    return true
                }

                if (arr[0] != arr[2]) {
                    println("Invalid Input")
                    return false
                }
                if (list[arr[3]][arr[2]] == 'W') {
                    println("Invalid Input")
                    return false
                }
            }
        }
        return true
    }

    private fun check(input: String): Boolean {
        if (checkInput(input) && checkMove(input)) return true
        return false
    }

    private fun move(input: String) {
        val arr = parse(input)
        when(state) {
            State.ACTION_PLAYER1 -> {
                list[arr[1]][arr[0]] = ' '
                list[arr[3]][arr[2]] = 'W'
            }
            State.ACTION_PLAYER2 -> {
                list[arr[1]][arr[0]] = ' '
                list[arr[3]][arr[2]] = 'B'
            }
        }
        log.add(arr)
    }

    fun printState() {
        when (state) {
            State.ACTION_PLAYER1 -> {
                print("$name1's turn:\n")
            }
            State.ACTION_PLAYER2 -> {
                print("$name2's turn:\n")
            }
            else -> {}
        }
    }

    fun action(input: String) {
        when {
            input == "exit" -> {
                state = State.EXIT
                println("Bye!")
            }
            !check(input) -> {}
            state == State.ACTION_PLAYER1 -> {
                move(input)
                state = State.ACTION_PLAYER2
                drawInitialBoard()
            }
            state == State.ACTION_PLAYER2 -> {
                move(input)
                state = State.ACTION_PLAYER1
                drawInitialBoard()
            }
        }
    }

    fun checkEndGame() {
        var winWhite = 0
        var winBlack = 0
        for (row in list) {
            if (row.contains('B')) winWhite += 1
            if (row.contains('W')) winBlack += 1
        }
        if (winWhite == 0 || list[7].contains('W')) {
            state == State.EXIT
            println("White Wins!")
            println("Bye!")
            return
        }
        if (winBlack == 0 || list[0].contains('B')) {
            state == State.EXIT
            println("Black Wins!")
            println("Bye!")
            return
        }
        var whitePawns = 0
        var whiteMoves =0

        for (i in 0..7) {
            for (j in 0..7) {
                if (list[i][j] == 'W') whitePawns += 1
                if (j > 0 && j < 7) {
                    if (list[i][j] == 'W' &&
                        list[i + 1][j] == 'B' &&
                        list[i + 1][j + 1] != 'B' &&
                        list[i + 1][j - 1] != 'B') {
                        whiteMoves += 1
                    }
                }
                if (j == 0) {
                    if (list[i][j] == 'W' &&
                        list[i + 1][j] == 'B' &&
                        list[i + 1][j + 1] != 'B') {
                        whiteMoves += 1
                    }
                }
                if (j == 7) {
                    if (list[i][j] == 'W' &&
                        list[i + 1][j] == 'B' &&
                        list[i + 1][j - 1] != 'B') {
                        whiteMoves += 1
                    }
                }
            }
        }
        var blackPawns = 0
        var blackMoves =0

        for (i in 0..7) {
            for (j in 0..7) {
                if (list[i][j] == 'B') blackPawns += 1
                if (j > 0 && j < 7) {
                    if (list[i][j] == 'B' &&
                        list[i - 1][j] == 'W' &&
                        list[i - 1][j + 1] != 'W' &&
                        list[i - 1][j - 1] != 'W') {
                        blackMoves += 1
                    }
                }
                if (j == 0) {
                    if (list[i][j] == 'B' &&
                        list[i - 1][j] == 'W' &&
                        list[i - 1][j + 1] != 'W') {
                        blackMoves += 1
                    }
                }
                if (j == 7) {
                    if (list[i][j] == 'B' &&
                        list[i - 1][j] == 'W' &&
                        list[i - 1][j - 1] != 'W') {
                        blackMoves += 1
                    }
                }
            }
        }
        if (whitePawns == whiteMoves || blackPawns == blackMoves) {
            state == State.EXIT
            println("Stalemate!")
            println("Bye!")
        }
    }
}