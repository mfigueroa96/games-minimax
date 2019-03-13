package com.example.games

inline fun<reified Celda > matrix2d(ancho: Int,
                                    alto: Int,
                                    noinline  param: (Int) -> Celda ): Array<Array<Celda>>
= Array(ancho){ Array<Celda>(alto, param ) }


enum class Ficha{
    CRUZ, BOLA, VACIO
}

enum class EstadoJuego{
    GANO_CRUZ, GANO_BOLA, EMPATADO, JUGANDO
}

class Celda(val renglon: Int, val columna: Int, var  estadoCelda: Ficha){


    fun limpiaCelda(){
        estadoCelda = Ficha.VACIO
    }

    override fun toString(): String {
         var s = when (this.estadoCelda) {
            Ficha.VACIO -> " V "
            Ficha.BOLA -> " O "
            Ficha.CRUZ -> " X "
        }
        return s
    }

}

class Tablero {
    val ancho = 3
    val alto = 3

    var celdas = matrix2d<Celda?>(ancho, alto){null}

    init{
        for(r in 0 until ancho){
            for (c in 0 until alto){
                celdas[r][c] = Celda(r,c, Ficha.VACIO)
            }
        }
    }
    fun init() {
        for (r in 0 until ancho) {
            for (c in 0 until alto) {
                celdas[r][c]?.limpiaCelda()
            }
        }
    }

    fun print(){
        for(r in 0 until ancho){
            for (c in 0 until alto){
                print(celdas[r][c] )
            }
            println()
        }
    }

    fun empate(): Boolean {
        for (r in 0 until ancho) {
            (0 until alto)
                .filter { celdas[r][it]?.estadoCelda == Ficha.VACIO}
                .forEach { return false }
        }
        return true
    }

    fun gano(player: Ficha): Boolean {
        for (i in 0..2)
            if(celdas[i][0]?.estadoCelda == player && celdas[i][1]?.estadoCelda == player && celdas[i][2]?.estadoCelda == player)
                return true

        for (i in 0..2)
            if(celdas[0][i]?.estadoCelda == player && celdas[1][i]?.estadoCelda == player && celdas[2][i]?.estadoCelda == player)
                return true

        if (celdas[0][0]?.estadoCelda == player && celdas[1][1]?.estadoCelda == player && celdas[2][2]?.estadoCelda == player
            || celdas[0][2]?.estadoCelda == player && celdas[1][1]?.estadoCelda == player && celdas[2][0]?.estadoCelda == player)
            return true
        return false
    }
    /*
*
* Completa el código
*
* La función debe de asignar en cada posición de la matriz de celdas que corresponda:
*
* Para p1, asignas Ficha.CRUZ
* Para p2, asignas Ficha.BOLA
*
* Completa: setTablero y setFecha, en setFicha utiliza un when
* */
    fun setTablero( p1: ArrayList<Int>,  p2: ArrayList<Int>){
        for (j in p1) {
            var i = j - 1;
            celdas[i / 3][i % 3]?.estadoCelda = Ficha.CRUZ
        }

        for (j in p2) {
            var i = j - 1;
            celdas[i / 3][i % 3]?.estadoCelda = Ficha.BOLA
        }
    }

    fun setFicha(posicion: Int,ficha: Ficha ){
        var col : Int = posicion / 3
        if (posicion in 1..3) celdas[0][col]?.estadoCelda = ficha
        if (posicion in 2..6) celdas[1][col]?.estadoCelda = ficha
        if (posicion in 1..3) celdas[2][col]?.estadoCelda = ficha
    }
}


class JugadorAutomatic(tablero: Tablero) {

    val tablero = tablero
    val renglones = tablero.ancho
    val columnas = tablero.alto

    var celdas = tablero.celdas

    var miFicha: Ficha
    var contrario: Ficha

    init {
        miFicha = Ficha.VACIO
        contrario = Ficha.VACIO
    }

    fun asignaFicha(ficha: Ficha) {
        miFicha = ficha
        contrario = when (miFicha) {
            Ficha.CRUZ-> Ficha.BOLA
            else -> Ficha.CRUZ
        }
    }

    fun calculaMovimiento(): Array<Int>? {
        val resultado = minimax(7, miFicha)
        tablero.print()
        return arrayOf(resultado[1], resultado[2])
    }

    fun minimax(profundidad: Int, jugador: Ficha): Array<Int> {
        val siguientesMovimientos: MutableList<Array<Int>> = generaMovimientos()

        var mejorCalificacion = when (miFicha) {
            jugador -> Int.MIN_VALUE
            else -> Int.MAX_VALUE
        }
        var calificacionActual: Int
        var mejorRenglon = -1
        var mejorColumna = -1

        if(siguientesMovimientos.isEmpty() || profundidad == 0){
            mejorCalificacion = evaluacion()
        } else {
            siguientesMovimientos.forEach {
                celdas[it[0]][it[1]]?.estadoCelda = jugador

                if (jugador == miFicha) {
                    /*
                    *
                    * Completa el código:
                    *
                    * Agrega el llamado recursivo de minimax y obtén la calificación Actual

                    * */
                    calificacionActual = minimax(profundidad - 1, contrario)[0]
                    if(calificacionActual > mejorCalificacion) {
                        mejorCalificacion = calificacionActual
                        mejorRenglon = it[0]
                        mejorColumna = it[1]
                    }
                } else {

                    /*
                    *
                    * Completa el código:
                    *
                    * Agrega el llamado recursivo de minimax y obtén la calificación Actual
                    *
                    * */
                    calificacionActual = minimax(profundidad - 1, miFicha)[0]
                    if(calificacionActual < mejorCalificacion) {
                        mejorCalificacion = calificacionActual
                        mejorRenglon = it[0]
                        mejorColumna = it[1]
                    }
                }
                celdas[it[0]][it[1]]?.estadoCelda = Ficha.VACIO
            }
        }
        return arrayOf(mejorCalificacion, mejorRenglon, mejorColumna)
    }

    fun generaMovimientos(): MutableList<Array<Int>> {
        val siguientesMovimientos: MutableList<Array<Int>> = mutableListOf()

        if (tablero.gano(miFicha) || tablero.gano(contrario)) {
            return siguientesMovimientos
        }

        for (r in 0 until renglones) {
            (0 until columnas)
                .asSequence()
                .filter { celdas[r][it]?.estadoCelda == Ficha.VACIO}
                .forEach { siguientesMovimientos.add(arrayOf(r, it)) }
        }
        return siguientesMovimientos
    }



    fun evaluacion(): Int {
        var calificacion = 0
        calificacion += evaluaLinea(0, 0, 0, 1, 0, 2)  // row 0
        calificacion += evaluaLinea(1, 0, 1, 1, 1, 2)  // row 1
        calificacion += evaluaLinea(2, 0, 2, 1, 2, 2)  // row 2
        calificacion += evaluaLinea(0, 0, 1, 0, 2, 0)  // col 0
        calificacion += evaluaLinea(0, 1, 1, 1, 2, 1)  // col 1
        calificacion += evaluaLinea(0, 2, 1, 2, 2, 2)  // col 2
        calificacion += evaluaLinea(0, 0, 1, 1, 2, 2)  // diagonal
        calificacion += evaluaLinea(0, 2, 1, 1, 2, 0)  // alternate diagonal
        return calificacion
    }

    fun evaluaLinea(r1: Int, col1: Int, row2: Int, col2: Int, row3: Int, col3: Int) : Int {
        var calificacion = 0

        if(celdas[r1][col1]?.estadoCelda == miFicha) {
            calificacion = 1
        } else if (celdas[r1][col1]?.estadoCelda == contrario) {
            calificacion = -1
        }

        if (celdas[row2][col2]?.estadoCelda == miFicha) {
            if (calificacion == 1) {
                calificacion = 10
            } else if (calificacion == -1) {
                return 0
            } else {
                calificacion = 1
            }
        } else if (celdas[row2][col2]?.estadoCelda == contrario) {
            if (calificacion == -1) {
                calificacion = -10
            } else if (calificacion == 1) {
                return 0
            } else {
                calificacion = -1
            }
        }

        if (celdas[row3][col3]?.estadoCelda == miFicha) {
            if (calificacion > 0) {
                calificacion *= 10
            } else if (calificacion < 0) {
                return 0
            } else {
                calificacion = 1
            }
        } else if (celdas[row3][col3]?.estadoCelda == contrario) {
            if (calificacion < 0) {
                calificacion *= 10
            } else if (calificacion > 1) {
                return 0
            } else {
                calificacion = -1
            }
        }
        return calificacion
    }

}
fun main(ags: Array<String>){

    println(Ficha.BOLA)
    println(EstadoJuego.EMPATADO)
    val c = Celda(0,0, Ficha.CRUZ)
    c.estadoCelda = Ficha.BOLA
    println(c)

    val tablero = Tablero()
    tablero.print()
    val computer = JugadorAutomatic(tablero)
    computer.asignaFicha(Ficha.BOLA)
    val salida = computer.calculaMovimiento()
    println( salida?.get(0))
    println( salida?.get(1))


}