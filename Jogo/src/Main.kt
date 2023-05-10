
import java.io.File

const val tenda = "T"
const val arvore = "A"
const val arvoreTerreno = " △ |"
const val tendaTerreno = " T |"
const val quebraDeLinha = "\n"
const val Horizontal = true
const val Vertical = true
const val perguntaIdade = "Qual a sua data de nascimento? (dd-mm-yyyy)"
const val perguntaCoordenadas = "Coordenadas da tenda? (ex: 1,B)"
const val aquiGanhamos = "Parabens! Terminou o jogo!\n"
const val saiString = "sair"
const val RespotaInvalida = "Resposta invalida"
const val DataInvalida = "Data invalida"
const val MenorDeIdadeNaoPodeJogar = "Menor de idade nao pode jogar"


fun processaCoordenadas(coordenadasStr: String?, numLines: Int, numColumns: Int): Pair<Int,Int>?{
    val resultado : Pair<Int,Int>? = when (coordenadasStr != null && coordenadasStr.length == 3){
        true -> {
            val codigoColunas = 64 + numColumns
            val validadePosicao0 = coordenadasStr[0].isDigit()
            val validaePosicao1 = coordenadasStr[1] == ','
            val validadePosicao2 = coordenadasStr[2].isUpperCase()
            when{
                !validadePosicao0 || !validaePosicao1 || !validadePosicao2 -> null
                coordenadasStr[2].code !in 65..codigoColunas -> null
                "${coordenadasStr[0]}".toInt() !in 1..numLines -> null
                else -> Pair("${coordenadasStr[0]}".toInt()-1,coordenadasStr[2].code-65)
            }
        }
        else -> null
    }
    return resultado
}
fun criaMenu(): String{
    return "\nBem vindo ao jogo das tendas\n\n1 - Novo jogo\n0 - Sair\n"
}
fun validaTamanhoMapa(numLinhas:Int, numColunas: Int): Boolean{
    return when{
        ( numLinhas == 6 && numColunas == 5) || (numLinhas==6 && numColunas == 6) ||
                (numLinhas == 8 && numColunas == 8) || (numLinhas == 10 && numColunas == 10)||
                (numLinhas == 8 && numColunas == 10) || (numLinhas == 10 && numColunas == 8)-> true

        else -> false
    }
}
fun validaDataNascimento(data: String?) : String?{
    var ano = 0
    var mes = 0
    var dia = 0
    var traco1 = ""
    var traco2 = ""
    return when {
        (data != null) && (data.length == 10) ->{
            ano = ("${data[6]}"+"${data[7]}"+"${data[8]}"+"${data[9]}").toInt()
            mes = ("${data[3]}"+"${data[4]}").toInt()
            dia = ("${data[0]}"+"${data[1]}").toInt()
            traco1 = "${data[2]}"
            traco2 = "${data[5]}"
            when {
                traco1 != "-" -> return DataInvalida
                traco2 != "-" -> return DataInvalida
                ano < 1900 -> return DataInvalida
                ano > 2022 -> return DataInvalida
                mes !in 1..12 -> return DataInvalida
                (mes == 1 || mes == 3 || mes == 5 || mes == 7 ||
                        mes == 8 || mes == 10 || mes == 12) && dia !in 1..31 -> return DataInvalida
                (mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia !in 1..30 -> return DataInvalida
                mes == 2 -> {
                    when{
                        ((ano % 4 == 0 && ano % 100 != 0) || ano % 400 == 0) && dia !in 1..29 -> return DataInvalida
                        !((ano % 4 == 0 && ano % 100 != 0) || ano % 400 == 0) && dia !in 1..28 -> return DataInvalida
                    }
                }
            }

            when (ano) {
                in 2005..2022 -> return MenorDeIdadeNaoPodeJogar
                in 1901..2003 -> return null
                2004 -> {
                    when (mes){
                        in 1..10 -> return null
                        in 11..12 -> return MenorDeIdadeNaoPodeJogar
                        else -> DataInvalida
                    }
                }
                else -> return DataInvalida
            }
        }
        else -> return DataInvalida
    }
}
fun criaLegendaHorizontal(numColunas: Int): String {
    var legenda  = ""
    var letra = 'A'
    var count = 1
    while (count <= numColunas) {
        if (count == numColunas) {
            legenda += "$letra"
        }else if (count == 1){
            legenda += "$letra | "
        }else{
            legenda += "$letra | "
        }
        letra++
        count++
    }
    return legenda
}
fun temTendaAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean{
    val linha = coords.first
    val coluna = coords.second
    if(linha>0 && terreno[linha-1][coluna] == "T") return true
    if(linha<terreno.size-1 && terreno[linha+1][coluna] == "T") return true
    if(coluna>0 && terreno[linha][coluna-1] == "T") return true
    if(coluna<terreno[0].size-1 && terreno[linha][coluna+1] == "T") return true
    if(linha>0 && coluna>0 && terreno[linha-1][coluna-1] == "T") return true
    if(linha<terreno.size-1 && coluna>0 && terreno[linha+1][coluna-1] == "T") return true
    if(linha>0 && coluna<terreno[0].size-1 && terreno[linha-1][coluna+1] == "T") return true
    if(linha<terreno.size-1 && coluna<terreno[0].size-1 && terreno[linha+1][coluna+1] == "T") return true
    return false

}
fun temArvoreAdjacente(terreno: Array<Array<String?>>, coords: Pair<Int, Int>) : Boolean{
    val linha = coords.first
    val coluna = coords.second
    if(coluna>0 && terreno[linha][coluna-1] == "A") return true
    if(coluna<terreno[0].size-1 && terreno[linha][coluna+1] == "A") return true
    return false
}
fun contaTendasColuna(terreno:Array<Array<String?>>, coluna: Int) : Int {
    var count = 0
    for(linha in 0 until terreno.size){
        if(terreno[linha][coluna] == "T"){
            count += 1
        }
    }
    return count
}
fun contaTendasLinha(terreno: Array<Array<String?>>, linha: Int): Int{
    var count = 0
    for(coluna in 0 until terreno[linha].size){
        if(terreno[linha][coluna] == "A"){
            count += 1
        }
    }
    return count
}
fun colocaTenda(terreno: Array<Array<String?>>, coords: Pair<Int, Int>): Boolean{
    val linha = coords.first
    val coluna = coords.second

    if(linha < 0 || linha > terreno.size-1 || coluna < 0 || coluna > terreno[0].size-1) return false
    if(terreno[linha][coluna] != null || !temArvoreAdjacente(terreno,coords)) return false
    if(temTendaAdjacente(terreno,coords)) return false

    terreno[linha][coluna] = "T"
    return true
}
fun leTerrenoDoFicheiro(numLines: Int, numColumns: Int): Array<Array<String?>> {
    val ficheiro = File("${numLines}x${numColumns}.txt").readLines()
    val terreno: Array<Array<String?>> = Array(numLines){
        arrayOfNulls(numColumns)
    }
    for (linhasf in 2 until ficheiro.size) {
        val cordenadas = ficheiro[linhasf].split(",")
        val linha = cordenadas[0].toInt()
        val coluna = cordenadas[1].toInt()
        terreno[linha][coluna] = "A"
    }
    return terreno
}
fun leContadoresDoFicheiro(numLines: Int, numColumns: Int, verticais: Boolean): Array<Int?> {
    val ficheiro = File("${numLines}x${numColumns}.txt").readLines()
    val contadores = arrayOfNulls<Int>(
        if (verticais) {
            numColumns
        } else {
            numLines
        }
    )
    val linhaFicheiro = if (verticais) 0 else 1
    val numeros = ficheiro[linhaFicheiro].split(",")
    for (count in 0 until numeros.size) {
        if(numeros[count].toInt() == 0){
            contadores[count] = null
        }else{
            contadores[count] = numeros[count].toInt()
        }

    }
    return contadores
}
fun criaLegendaContadoresHorizontal(contadoresHorizontal: Array<Int?>): String {
    var resultado = ""
    for (posicaoColuna in 0 until contadoresHorizontal.size) {
        resultado += when {
            contadoresHorizontal[posicaoColuna] == null -> " "
            else -> contadoresHorizontal[posicaoColuna]
        }
        if (posicaoColuna != contadoresHorizontal.size-1) {
            resultado += "   "
        }
    }
    return resultado
}
fun criaTerreno(terreno: Array<Array<String?>>, contadoresVerticais: Array<Int?>?, contadoresHorizontais: Array<Int?>?,
                mostraLegendaHorizontal: Boolean, mostraLegendaVertical: Boolean): String {
    var resultado = ""
    when {
        mostraLegendaHorizontal && contadoresVerticais == null -> {
            resultado +=  "${espacos(4)}| " + criaLegendaHorizontal(terreno[0].size) + quebraDeLinha
        }

        mostraLegendaHorizontal && contadoresVerticais != null -> {
            resultado += espacos(6)+ criaLegendaContadoresHorizontal(contadoresVerticais) + quebraDeLinha +
                    "${espacos(4)}| " + criaLegendaHorizontal(terreno[0].size) + quebraDeLinha
        }

        contadoresVerticais != null -> {
            resultado += espacos(6) + criaLegendaContadoresHorizontal(contadoresVerticais) + quebraDeLinha
        }
    }
    for (numLinha in 0 until terreno.size) {
        when {
            !mostraLegendaVertical && contadoresHorizontais == null -> resultado += espacos(4) + "|"
            mostraLegendaVertical && contadoresHorizontais == null -> {
                if (numLinha >= 9) {
                    resultado += "  ${numLinha + 1} |"
                } else {
                    resultado += "   ${numLinha + 1} |"
                }
            }
            !mostraLegendaVertical && contadoresHorizontais != null -> {
                val contador = contadoresHorizontais[numLinha]
                if (contador == null) {
                    resultado += "     |"
                } else {
                    resultado += "$contador${espacos(4)}|"
                }
            }
            mostraLegendaVertical && contadoresHorizontais != null -> {
                val contador = contadoresHorizontais[numLinha]
                if (contador != null) {
                    if (numLinha >= 9) {
                        resultado += "$contador ${numLinha + 1} |"
                    } else {
                        resultado += "$contador  ${numLinha + 1} |"
                    }
                } else {
                    if (numLinha >= 9) {
                        resultado += "  ${numLinha + 1} |"
                    } else {
                        resultado += "   ${numLinha + 1} |"
                    }
                }
            }
        }
        for (numColuna in 0 until terreno[numLinha].size) {
            if (numColuna < terreno[0].size - 1) {
                if (terreno[numLinha][numColuna] == null) resultado += "${espacos(2)}|"
                if (terreno[numLinha][numColuna] == tenda) resultado += tendaTerreno
                if (terreno[numLinha][numColuna] == arvore) resultado += arvoreTerreno
            } else {
                if (numLinha < terreno.size - 1) {
                    if (terreno[numLinha][numColuna] == null) resultado += "${espacos(1)}${quebraDeLinha}"
                    if (terreno[numLinha][numColuna] == tenda) resultado += " T${quebraDeLinha}"
                    if (terreno[numLinha][numColuna] == arvore) resultado += " △${quebraDeLinha}"
                } else {
                    if (terreno[numLinha][numColuna] == null) resultado += espacos(1) + quebraDeLinha
                    if (terreno[numLinha][numColuna] == tenda) resultado += " T" + quebraDeLinha
                    if (terreno[numLinha][numColuna] == arvore) resultado += " △" + quebraDeLinha
                }
                if (numLinha == terreno.size-1){
                    resultado += "  ----"
                    for (colunas in 0 until terreno[0].size){
                        resultado += when(colunas){
                            terreno[0].size -1 -> "--"
                            else -> "----"
                        }
                    }
                }
            }
        }
    }
    return resultado
}

//funções extras
fun coordenadasFDP(coordenadasStr: String?, numLines: Int, numColumns: Int): Pair<Int, Int> {
    var resultado : Pair<Int,Int> = Pair(0,0)
    if (coordenadasStr != null && coordenadasStr.length == 3){
        resultado = Pair("${coordenadasStr[0]}".toInt()-1,coordenadasStr[2].code-65)
    }
    return resultado
}
fun espacos(espacos: Int): String{
    var resultado = ""
    var count = 0

    do {
        resultado += " "
        count ++
    }while (count<=espacos)

    return resultado
}
fun verificaLinhas(numLinhas: Int?): Boolean{
    val linhas : Boolean = when {
        numLinhas == null || numLinhas <= 0 -> false
        else -> true
    }
    return linhas
}
fun verificaColunas(numColunas: Int?):Boolean{
    val colunas :Boolean = when {
        numColunas == null || numColunas <= 0 -> false
        else -> true
    }
    return colunas
}
fun sair (coordenadasStr: String?): Int {
    var resultado = 1
    when (coordenadasStr) {
        "sair" -> resultado = 0
        else -> resultado = 1
    }
    return resultado
}

fun terminouJogo(terreno: Array<Array<String?>>, contadoresVerticais: Array<Int?>, contadoresHorizontais: Array<Int?>): Boolean{
    for (numLinhas in 0 until terreno.size){
        val tendasLinha = contaTendasLinha(terreno, numLinhas)
        val contadorH = contadoresHorizontais[numLinhas] ?: 0
        if (tendasLinha != contadorH) {
            return false
        }
    }
    for (numColunas in 0 until contadoresHorizontais.size){
        val tendasColuna = contaTendasColuna(terreno,numColunas)
        val contadorV = contadoresVerticais[numColunas] ?: 0
        if (tendasColuna != contadorV){
            return false
        }
    }
    return true
}
fun main() {
    do {
        var recomeca = false
        var idade : String? = ""
        println(criaMenu())
        var menu = readln().toIntOrNull()
        if (menu == null || menu !in 0..1){ println("Opcao invalida")
            recomeca = true }
        when (menu) {
            1 -> {
                println("Quantas linhas?")
                var linhas = readln().toIntOrNull()
                while (!verificaLinhas(linhas)) {
                    println("$RespotaInvalida\nQuantas linhas?")
                    linhas = readln().toIntOrNull()
                }
                println("Quantas colunas?")
                var colunas = readln().toIntOrNull()
                while (!verificaColunas(colunas)) {
                    println("$RespotaInvalida\nQuantas colunas?")
                    colunas = readln().toIntOrNull()
                }
                if (linhas != null && colunas != null) {
                    if (!validaTamanhoMapa(linhas, colunas)) {
                        println("Terreno invalido")
                        recomeca = true
                    }
                    val terreno = leTerrenoDoFicheiro(linhas, colunas)
                    val contadoresV = leContadoresDoFicheiro(linhas,colunas,true)
                    val contadoresH = leContadoresDoFicheiro(linhas,colunas,false)
                    var mapa = criaTerreno(terreno,contadoresV,contadoresH,Horizontal,Vertical)
                    if (linhas == 10 && colunas == 10) {
                        do {
                            println(perguntaIdade)
                            val data = readln()
                            when(validaDataNascimento(data)){
                                null -> idade = null
                                MenorDeIdadeNaoPodeJogar -> {
                                    println(MenorDeIdadeNaoPodeJogar)
                                    recomeca = true
                                }
                                DataInvalida -> println(validaDataNascimento(data))
                            }
                        } while (validaDataNascimento(data) == DataInvalida)
                        if (idade == null && validaTamanhoMapa(linhas, colunas)) {
                            println("\n" + mapa)
                            do {
                                println(perguntaCoordenadas)
                                val coordenadas = readln()
                                if (coordenadas == saiString) {
                                    menu = 0
                                } else if (processaCoordenadas(coordenadas, linhas, colunas) == null) {
                                    println("Coordenadas invalidas")
                                }else{
                                    val  pairCoordenadas = coordenadasFDP(coordenadas, linhas, colunas)
                                    when (colocaTenda(terreno, pairCoordenadas)) {
                                        false -> println("Tenda nao pode ser colocada nestas coordenadas")
                                        else -> {
                                            terreno[pairCoordenadas.first][pairCoordenadas.second] = "T"
                                            mapa = criaTerreno(terreno, contadoresV, contadoresH, Horizontal, Vertical)
                                            print(quebraDeLinha+mapa+quebraDeLinha)
                                        }
                                    }
                                }
                            } while ((processaCoordenadas(coordenadas, linhas, colunas) == null
                                        && terminouJogo(terreno, contadoresV, contadoresH)) ||
                                coordenadas != saiString && !terminouJogo(terreno, contadoresV, contadoresH))
                            if (terminouJogo(terreno, contadoresV, contadoresH)) {
                                print(aquiGanhamos)
                                recomeca = true
                            }
                        }
                    }else {
                        println(quebraDeLinha + mapa)
                        do {
                            println(perguntaCoordenadas)
                            val coordenadas = readln()
                            if (coordenadas == saiString) {
                                menu = 0
                            } else if (processaCoordenadas(coordenadas, linhas, colunas) == null) {
                                println("Coordenadas invalidas")
                            }else{
                                val pairCoordenadas = coordenadasFDP(coordenadas, linhas, colunas)
                                when (colocaTenda(terreno, pairCoordenadas)) {
                                    false -> println("Tenda nao pode ser colocada nestas coordenadas")
                                    else -> {
                                        terreno[pairCoordenadas.first][pairCoordenadas.second] = "T"
                                        mapa = criaTerreno(terreno, contadoresV, contadoresH, Horizontal, Vertical)
                                        print(quebraDeLinha+mapa+quebraDeLinha)
                                    }
                                }
                            }
                        } while ((processaCoordenadas(coordenadas, linhas, colunas) == null
                                    && terminouJogo(terreno, contadoresV, contadoresH)) ||
                            coordenadas != saiString && !terminouJogo(terreno, contadoresV, contadoresH))
                        if (terminouJogo(terreno, contadoresV, contadoresH)) {
                            print(aquiGanhamos)
                            recomeca = true
                        }
                    }
                }
            }
        }
    }while (recomeca)
}