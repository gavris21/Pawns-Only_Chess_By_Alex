package chess

fun main() {
//    write your code here
    print("Pawns-Only Chess\nFirst Player's name:\n> ")
    val name1 = readLine()!!
    print("Second Player's name:\n> ")
    val name2 = readLine()!!
    val chess = Chess(name1, name2)
    chess.drawInitialBoard()
    while (chess.state != State.EXIT) {
        chess.printState()
        val input = readLine()!!
        chess.action(input)
        chess.checkEndGame()
    }
}