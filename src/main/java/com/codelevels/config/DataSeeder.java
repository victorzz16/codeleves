package com.codelevels.config;

import com.codelevels.model.AppUser;
import com.codelevels.model.Challenge;
import com.codelevels.model.Role;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final AppUserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        ensureUser("Estudiante UTP", "estudiante@utp.edu.pe", "123456", Role.ALUMNO);
        ensureUser("Docente UTP", "profesor@utp.edu.pe", "123456", Role.DOCENTE);
        ensureUser("Administrador CodeLevels", "admin@codelevels.pe", "123456", Role.ADMIN);

        ensureChallenge("Sumar dos números", "Básico", "Entrada / Salida",
                "Pide al usuario dos números enteros y muestra su suma en pantalla.",
                "[\"▶ Inicio\",\"📥 Leer primer número\",\"📥 Leer segundo número\",\"➕ Sumar n1 + n2\",\"📤 Mostrar resultado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Primero necesitas leer los dos datos.\",\"Luego realiza la suma.\",\"Finalmente muestra el resultado.\"]",
                "public class SumarDosNumeros { public static void main(String[] args) { /* Scanner, leer n1 y n2, sumar y mostrar */ } }");

        ensureChallenge("Mayor o menor de edad", "Básico", "Condicional",
                "Pide la edad del usuario y determina si es mayor o menor de edad.",
                "[\"▶ Inicio\",\"📥 Leer edad\",\"🔀 Evaluar edad >= 18\",\"📤 Mostrar mayor de edad\",\"📤 Mostrar menor de edad\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Primero lee la edad.\",\"Después compara con 18.\",\"Muestra una salida para cada caso.\"]",
                "public class MayorEdad { public static void main(String[] args) { if (edad >= 18) { System.out.println(\"Mayor\"); } else { System.out.println(\"Menor\"); } } }");

        ensureChallenge("Aplicar descuento", "Básico", "Condicional",
                "Calcula el precio final de un producto aplicando 10% de descuento si supera S/ 100.",
                "[\"▶ Inicio\",\"📥 Leer precio\",\"🔀 Evaluar precio > 100\",\"➕ Calcular descuento 10%\",\"📤 Mostrar precio final\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Primero lee el precio.\",\"El descuento solo se aplica si se cumple la condición.\",\"Muestra siempre el precio final.\"]",
                "public class Descuento { public static void main(String[] args) { double finalPrecio = precio > 100 ? precio * 0.90 : precio; } }");

        ensureChallenge("Número par o impar", "Básico", "Operador módulo",
                "Pide un número y determina si es par o impar usando el residuo de la división.",
                "[\"▶ Inicio\",\"📥 Leer número\",\"➕ Calcular n % 2\",\"🔀 Evaluar residuo == 0\",\"📤 Mostrar par\",\"📤 Mostrar impar\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Usa el operador módulo %.\",\"Si el residuo es 0, el número es par.\",\"Si no, es impar.\"]",
                "public class ParImpar { public static void main(String[] args) { if (n % 2 == 0) System.out.println(\"Par\"); else System.out.println(\"Impar\"); } }");

        ensureChallenge("Promedio de tres notas", "Medio", "Operaciones",
                "Pide tres notas, calcula el promedio y determina si aprobó.",
                "[\"▶ Inicio\",\"📥 Leer nota 1\",\"📥 Leer nota 2\",\"📥 Leer nota 3\",\"➕ Calcular promedio\",\"🔀 Evaluar promedio >= 11\",\"📤 Mostrar aprobado\",\"📤 Mostrar desaprobado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8]",
                "[\"Lee las tres notas antes de calcular.\",\"Calcula el promedio.\",\"Evalúa si el promedio aprueba.\"]",
                "public class PromedioNotas { public static void main(String[] args) { double promedio = (n1+n2+n3)/3; } }");

        ensureChallenge("Tabla de multiplicar", "Medio", "Bucle",
                "Pide un número y muestra su tabla de multiplicar del 1 al 10.",
                "[\"▶ Inicio\",\"📥 Leer número\",\"🔁 Repetir de 1 a 10\",\"➕ Multiplicar n * i\",\"📤 Mostrar cada resultado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Necesitas leer el número base.\",\"Luego usa un bucle.\",\"Dentro del bucle multiplica y muestra.\"]",
                "public class TablaMultiplicar { public static void main(String[] args) { for(int i=1;i<=10;i++){ System.out.println(n*i); } } }");

        ensureChallenge("Suma de 1 hasta N", "Medio", "Bucle acumulador",
                "Pide un valor N y suma todos los números desde 1 hasta N.",
                "[\"▶ Inicio\",\"📥 Leer N\",\"📦 Inicializar suma = 0\",\"🔁 Repetir i de 1 a N\",\"➕ Acumular suma = suma + i\",\"📤 Mostrar suma\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Necesitas una variable acumuladora.\",\"El bucle recorre de 1 hasta N.\",\"La salida se muestra al final.\"]",
                "public class SumaHastaN { public static void main(String[] args) { int suma=0; for(int i=1;i<=n;i++){ suma+=i; } } }");

        ensureChallenge("Factorial de un número", "Medio", "Bucle acumulador",
                "Calcula el factorial de un número entero positivo.",
                "[\"▶ Inicio\",\"📥 Leer N\",\"📦 Inicializar factorial = 1\",\"🔁 Repetir i de 1 a N\",\"➕ Multiplicar factorial *= i\",\"📤 Mostrar factorial\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"El factorial empieza en 1.\",\"Multiplica en cada vuelta del bucle.\",\"No inicialices en 0 porque todo quedaría 0.\"]",
                "public class Factorial { public static void main(String[] args) { long f=1; for(int i=1;i<=n;i++){ f*=i; } } }");

        ensureChallenge("Mayor de tres números", "Medio", "Condicional múltiple",
                "Pide tres números y determina cuál es el mayor.",
                "[\"▶ Inicio\",\"📥 Leer A\",\"📥 Leer B\",\"📥 Leer C\",\"🔀 Evaluar A > B y A > C\",\"🔀 Evaluar B > C\",\"📤 Mostrar A\",\"📤 Mostrar B\",\"📤 Mostrar C\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8,9]",
                "[\"Compara primero A con B y C.\",\"Si A no es mayor, compara B con C.\",\"El último caso corresponde a C.\"]",
                "public class MayorTres { public static void main(String[] args) { if(a>b && a>c){} else if(b>c){} else{} } }");

        ensureChallenge("Calculadora básica", "Medio", "Switch",
                "Permite elegir una operación: sumar, restar, multiplicar o dividir.",
                "[\"▶ Inicio\",\"📥 Leer número A\",\"📥 Leer número B\",\"📥 Leer opción\",\"🔀 Evaluar opción con switch\",\"➕ Ejecutar operación\",\"📤 Mostrar resultado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Lee primero los números y la opción.\",\"Usa switch para seleccionar operación.\",\"Muestra el resultado calculado.\"]",
                "public class Calculadora { public static void main(String[] args) { switch(opcion){ case 1 -> suma=a+b; } } }");

        ensureChallenge("Validar contraseña", "Medio", "Condicional",
                "Pide una contraseña y valida si coincide con una clave registrada.",
                "[\"▶ Inicio\",\"📥 Leer contraseña\",\"🔀 Comparar con clave correcta\",\"📤 Mostrar acceso permitido\",\"📤 Mostrar acceso denegado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Primero lee la contraseña.\",\"Compara texto usando equals en Java.\",\"Muestra un mensaje según el resultado.\"]",
                "public class LoginSimple { public static void main(String[] args) { if(clave.equals(input)){} } }");

        ensureChallenge("IMC básico", "Medio", "Operaciones",
                "Calcula el índice de masa corporal a partir del peso y la talla.",
                "[\"▶ Inicio\",\"📥 Leer peso\",\"📥 Leer talla\",\"➕ Calcular imc = peso / talla²\",\"🔀 Clasificar IMC\",\"📤 Mostrar diagnóstico\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Necesitas peso y talla.\",\"La fórmula usa talla al cuadrado.\",\"Después clasifica el resultado.\"]",
                "public class IMC { public static void main(String[] args) { double imc = peso / (talla*talla); } }");

        ensureChallenge("Contar números pares", "Avanzado", "Bucle + condicional",
                "Cuenta cuántos números pares hay entre 1 y N.",
                "[\"▶ Inicio\",\"📥 Leer N\",\"📦 Inicializar contador = 0\",\"🔁 Repetir i de 1 a N\",\"🔀 Evaluar i % 2 == 0\",\"➕ Incrementar contador\",\"📤 Mostrar contador\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Combina bucle y condicional.\",\"Solo incrementas si el número es par.\",\"La salida va después del bucle.\"]",
                "public class ContadorPares { public static void main(String[] args) { int c=0; for(int i=1;i<=n;i++){ if(i%2==0)c++; } } }");

        ensureChallenge("Menú repetitivo", "Avanzado", "Do while",
                "Muestra un menú hasta que el usuario elija salir.",
                "[\"▶ Inicio\",\"🔁 Mostrar menú en do-while\",\"📥 Leer opción\",\"🔀 Evaluar opción\",\"➕ Ejecutar acción\",\"🔀 Verificar si opción != salir\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"El menú debe repetirse.\",\"Usa do-while para ejecutar al menos una vez.\",\"Termina cuando el usuario elige salir.\"]",
                "public class Menu { public static void main(String[] args) { do { } while(opcion != 0); } }");

        ensureChallenge("Adivinar número", "Avanzado", "While",
                "Permite intentar adivinar un número secreto hasta acertar.",
                "[\"▶ Inicio\",\"📦 Definir número secreto\",\"📥 Leer intento\",\"🔁 Mientras intento != secreto\",\"📤 Dar pista\",\"📥 Leer nuevo intento\",\"📤 Mostrar acierto\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Necesitas un número secreto.\",\"El while continúa hasta acertar.\",\"Da pistas para orientar al usuario.\"]",
                "public class Adivina { public static void main(String[] args) { while(intento != secreto){} } }");

        ensureChallenge("Guardar 5 números en arreglo", "Arreglos", "Arrays",
                "Declara un arreglo de 5 enteros, pide valores y los muestra.",
                "[\"▶ Inicio\",\"📦 Declarar arreglo int[5]\",\"🔁 Repetir 5 veces para llenar\",\"📥 Leer y guardar nums[i]\",\"🔁 Repetir 5 veces para mostrar\",\"📤 Mostrar nums[i]\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"El índice empieza en 0.\",\"Usa un bucle para llenar.\",\"Usa otro bucle para mostrar.\"]",
                "public class ArregloBasico { public static void main(String[] args) { int[] nums=new int[5]; } }");

        ensureChallenge("Sumar elementos de arreglo", "Arreglos", "Arrays",
                "Llena un arreglo de 5 números y calcula la suma total.",
                "[\"▶ Inicio\",\"📦 Declarar arreglo\",\"📦 Inicializar suma = 0\",\"🔁 Llenar arreglo\",\"📥 Leer nums[i]\",\"🔁 Recorrer arreglo\",\"➕ Acumular suma += nums[i]\",\"📤 Mostrar suma\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8]",
                "[\"Primero llena el arreglo.\",\"Luego recórrelo para sumar.\",\"Muestra la suma al final.\"]",
                "public class SumaArreglo { public static void main(String[] args) { for(int x: nums) suma += x; } }");

        ensureChallenge("Mayor de un arreglo", "Arreglos", "Arrays",
                "Encuentra el valor mayor dentro de un arreglo de números.",
                "[\"▶ Inicio\",\"📦 Declarar arreglo\",\"🔁 Llenar arreglo\",\"📥 Leer nums[i]\",\"📦 mayor = nums[0]\",\"🔁 Recorrer desde índice 1\",\"🔀 Evaluar nums[i] > mayor\",\"➕ Actualizar mayor\",\"📤 Mostrar mayor\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8,9]",
                "[\"Inicializa mayor con nums[0].\",\"Compara desde el índice 1.\",\"Actualiza solo si encuentras un valor más grande.\"]",
                "public class MayorArreglo { public static void main(String[] args) { int mayor=nums[0]; } }");

        ensureChallenge("Buscar valor en arreglo", "Arreglos", "Arrays",
                "Busca si un número ingresado existe dentro de un arreglo.",
                "[\"▶ Inicio\",\"📦 Declarar arreglo\",\"📥 Leer valor buscado\",\"🔁 Recorrer arreglo\",\"🔀 Comparar nums[i] == buscado\",\"📤 Mostrar encontrado\",\"📤 Mostrar no encontrado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Necesitas el valor a buscar.\",\"Recorre cada posición del arreglo.\",\"Usa una bandera encontrado.\"]",
                "public class BuscarArreglo { public static void main(String[] args) { boolean encontrado=false; } }");

        ensureChallenge("Promedio con arreglo", "Arreglos", "Arrays",
                "Calcula el promedio de notas almacenadas en un arreglo.",
                "[\"▶ Inicio\",\"📦 Declarar arreglo de notas\",\"📦 Inicializar suma = 0\",\"🔁 Llenar notas\",\"📥 Leer nota[i]\",\"🔁 Recorrer notas\",\"➕ Sumar nota[i]\",\"➕ Calcular promedio\",\"📤 Mostrar promedio\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8,9]",
                "[\"Usa arreglo para guardar varias notas.\",\"Suma todas las notas.\",\"Divide entre la cantidad de elementos.\"]",
                "public class PromedioArreglo { public static void main(String[] args) { double promedio = suma / notas.length; } }");

        ensureChallenge("Crear método saludar", "Métodos", "Funciones",
                "Crea un método que reciba un nombre y muestre un saludo personalizado.",
                "[\"▶ Inicio\",\"📦 Declarar método saludar(nombre)\",\"📥 Leer nombre\",\"➕ Llamar método saludar(nombre)\",\"📤 Mostrar saludo\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5]",
                "[\"Primero define el método.\",\"Lee el dato que se enviará.\",\"Llama el método con el parámetro.\"]",
                "public class Metodos { static void saludar(String nombre){ System.out.println(\"Hola \"+nombre); } }");

        ensureChallenge("Método calcular promedio", "Métodos", "Funciones",
                "Crea un método que reciba tres notas y retorne el promedio.",
                "[\"▶ Inicio\",\"📦 Declarar método promedio(n1,n2,n3)\",\"📥 Leer tres notas\",\"➕ Llamar método promedio\",\"📦 Guardar valor retornado\",\"📤 Mostrar promedio\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"El método debe retornar un valor.\",\"Guarda el resultado en una variable.\",\"Después muestra ese resultado.\"]",
                "public class MetodoPromedio { static double promedio(double a,double b,double c){ return (a+b+c)/3; } }");

        ensureChallenge("Matriz 2x2", "Matrices", "Arrays 2D",
                "Lee una matriz 2x2 y muestra todos sus elementos.",
                "[\"▶ Inicio\",\"📦 Declarar matriz int[2][2]\",\"🔁 Recorrer filas\",\"🔁 Recorrer columnas\",\"📥 Leer matriz[f][c]\",\"🔁 Recorrer para mostrar\",\"📤 Mostrar matriz[f][c]\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Una matriz usa dos índices.\",\"Necesitas bucles anidados.\",\"Primero llenas y luego muestras.\"]",
                "public class Matriz { public static void main(String[] args) { int[][] m = new int[2][2]; } }");



        // Retos Premium alineados con el sílabo de Principios de Algoritmos UTP:
        // estructura secuencial, condicional y repetitiva.
        ensurePremiumChallenge("Lógica proposicional aplicada", "Pro", "Lógica proposicional",
                "Evalúa proposiciones compuestas usando conjunción, disyunción y negación para decidir si un estudiante puede rendir una evaluación.",
                "[\"▶ Inicio\",\"📥 Leer asistencia\",\"📥 Leer entrega de tarea\",\"🔀 Evaluar asistencia >= 70 Y tarea entregada\",\"📤 Mostrar habilitado\",\"📤 Mostrar no habilitado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"La conjunción exige que ambas condiciones sean verdaderas.\",\"Representa Y con el operador lógico &&.\",\"Incluye una salida para el caso falso.\"]",
                "public class LogicaProposicionalPro { public static void main(String[] args) { boolean habilitado = asistencia >= 70 && tareaEntregada; System.out.println(habilitado ? \"Habilitado\" : \"No habilitado\"); } }");

        ensurePremiumChallenge("Tipos de datos y variables", "Pro", "Estructura secuencial",
                "Declara variables de tipo entero, real, cadena y booleano para registrar los datos básicos de un estudiante.",
                "[\"▶ Inicio\",\"📦 Declarar String nombre\",\"📦 Declarar int edad\",\"📦 Declarar double promedio\",\"📦 Declarar boolean aprobado\",\"📥 Asignar valores\",\"📤 Mostrar ficha\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Relaciona cada dato con el tipo adecuado.\",\"Una edad no necesita decimales.\",\"El estado aprobado puede representarse con boolean.\"]",
                "public class VariablesPro { public static void main(String[] args) { String nombre=\"Ana\"; int edad=18; double promedio=15.5; boolean aprobado=promedio>=12; } }");

        ensurePremiumChallenge("Conversión de segundos", "Pro", "Pseudocódigo secuencial",
                "Convierte una cantidad total de segundos a horas, minutos y segundos restantes mediante operaciones secuenciales.",
                "[\"▶ Inicio\",\"📥 Leer totalSegundos\",\"➗ Calcular horas = total / 3600\",\"➗ Calcular resto = total % 3600\",\"➗ Calcular minutos = resto / 60\",\"➗ Calcular segundos = resto % 60\",\"📤 Mostrar resultado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Usa división entera para obtener horas y minutos.\",\"Usa módulo para calcular los residuos.\",\"Todas las operaciones se ejecutan en orden.\"]",
                "public class ConversionTiempoPro { public static void main(String[] args) { int horas=total/3600; int resto=total%3600; int minutos=resto/60; int segundos=resto%60; } }");

        ensurePremiumChallenge("Diagrama de flujo de una compra", "Pro", "Diagramas de flujo",
                "Organiza los símbolos lógicos de un diagrama de flujo para calcular subtotal, IGV y total de una compra.",
                "[\"⬭ Inicio\",\"▱ Leer precio y cantidad\",\"▭ Calcular subtotal\",\"▭ Calcular IGV = subtotal * 0.18\",\"▭ Calcular total\",\"▱ Mostrar subtotal, IGV y total\",\"⬭ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"El óvalo representa inicio y fin.\",\"El paralelogramo representa entrada o salida.\",\"El rectángulo representa un proceso.\"]",
                "public class CompraPro { public static void main(String[] args) { double subtotal=precio*cantidad; double igv=subtotal*0.18; double total=subtotal+igv; } }");

        ensurePremiumChallenge("Bono por ventas", "Pro", "Condicional simple",
                "Calcula un bono del 5% únicamente cuando las ventas del trabajador superan S/ 5 000.",
                "[\"▶ Inicio\",\"📥 Leer ventas\",\"📦 Inicializar bono = 0\",\"🔀 Si ventas > 5000\",\"➕ Calcular bono = ventas * 0.05\",\"📤 Mostrar bono\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Es una condición simple: puede no ejecutarse el cálculo.\",\"Inicializa el bono antes del if.\",\"La salida se muestra al final.\"]",
                "public class BonoVentasPro { public static void main(String[] args) { double bono=0; if(ventas>5000){ bono=ventas*0.05; } System.out.println(bono); } }");

        ensurePremiumChallenge("Tarifa de estacionamiento", "Pro", "Condicional doble",
                "Calcula la tarifa: S/ 5 por hasta dos horas y S/ 3 por cada hora adicional.",
                "[\"▶ Inicio\",\"📥 Leer horas\",\"🔀 Si horas <= 2\",\"➕ Total = 5\",\"➕ Si no, total = 5 + (horas-2)*3\",\"📤 Mostrar total\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"La estructura doble utiliza si y si no.\",\"No cobres horas adicionales dentro de las dos primeras.\",\"Valida que las horas sean positivas.\"]",
                "public class EstacionamientoPro { public static void main(String[] args) { double total = horas<=2 ? 5 : 5+(horas-2)*3; } }");

        ensurePremiumChallenge("Clasificación de promedio", "Pro", "Condicional anidada",
                "Clasifica una nota como excelente, aprobado, en recuperación o desaprobado usando condiciones anidadas.",
                "[\"▶ Inicio\",\"📥 Leer nota\",\"🔀 Si nota >= 18\",\"📤 Mostrar Excelente\",\"🔀 Si no, evaluar nota >= 12\",\"📤 Mostrar Aprobado\",\"🔀 Si no, evaluar nota >= 10\",\"📤 Mostrar Recuperación\",\"📤 Mostrar Desaprobado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8,9]",
                "[\"Evalúa primero el rango más alto.\",\"Usa else if para evitar resultados duplicados.\",\"La nota debe estar entre 0 y 20.\"]",
                "public class ClasificacionNotaPro { public static void main(String[] args) { if(nota>=18){} else if(nota>=12){} else if(nota>=10){} else{} } }");

        ensurePremiumChallenge("Menú de áreas geométricas", "Pro", "Condicional múltiple",
                "Usa una estructura múltiple para calcular el área de un cuadrado, rectángulo o círculo.",
                "[\"▶ Inicio\",\"📋 Mostrar opciones 1, 2 y 3\",\"📥 Leer opción\",\"🔀 Evaluar opción con switch\",\"📥 Leer dimensiones necesarias\",\"➕ Calcular área\",\"📤 Mostrar área o error\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Cada opción requiere datos distintos.\",\"Agrega un caso default.\",\"Para el círculo usa Math.PI.\"]",
                "public class AreasPro { public static void main(String[] args) { switch(opcion){ case 1 -> area=lado*lado; case 2 -> area=base*altura; case 3 -> area=Math.PI*radio*radio; default -> area=-1; } } }");

        ensurePremiumChallenge("Serie y suma con PARA", "Pro", "Estructura PARA",
                "Muestra los números del 1 al N y calcula su suma utilizando una estructura repetitiva con contador.",
                "[\"▶ Inicio\",\"📥 Leer N\",\"📦 Inicializar suma = 0\",\"🔁 Para i = 1 hasta N\",\"📤 Mostrar i\",\"➕ Acumular suma += i\",\"📤 Mostrar suma\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"PARA es apropiado cuando conoces el número de repeticiones.\",\"Inicializa el acumulador fuera del bucle.\",\"Muestra el total al terminar.\"]",
                "public class SumaParaPro { public static void main(String[] args) { int suma=0; for(int i=1;i<=n;i++){ System.out.println(i); suma+=i; } } }");

        ensurePremiumChallenge("Validación con MIENTRAS", "Pro", "Estructura MIENTRAS",
                "Solicita una nota hasta que el usuario ingrese un valor válido entre 0 y 20.",
                "[\"▶ Inicio\",\"📥 Leer nota\",\"🔁 Mientras nota < 0 O nota > 20\",\"📤 Mostrar mensaje de error\",\"📥 Volver a leer nota\",\"📤 Mostrar nota válida\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"MIENTRAS evalúa la condición antes de repetir.\",\"La condición de error usa OR.\",\"Actualiza la nota dentro del bucle.\"]",
                "public class ValidarNotaPro { public static void main(String[] args) { while(nota<0 || nota>20){ /* volver a leer */ } } }");

        ensurePremiumChallenge("Encuesta con REPETIR-HASTA", "Pro", "Repetir hasta que",
                "Registra respuestas de una encuesta y repite el proceso hasta que el usuario confirme que desea terminar.",
                "[\"▶ Inicio\",\"🔁 Repetir\",\"📥 Leer respuesta\",\"➕ Registrar respuesta\",\"📥 Preguntar si desea continuar\",\"🔀 Hasta que continuar = NO\",\"📤 Mostrar total registrado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"El bloque se ejecuta al menos una vez.\",\"Pregunta la condición al final.\",\"En Java puedes representarlo con do-while.\"]",
                "public class EncuestaPro { public static void main(String[] args) { do { /* registrar */ } while(continuar); } }");

        ensurePremiumChallenge("Evaluación integradora UTP", "Pro", "Secuencial + condicional + repetitiva",
                "Procesa las notas de varios estudiantes, calcula promedios, determina aprobados y muestra estadísticas finales.",
                "[\"▶ Inicio\",\"📥 Leer cantidad de estudiantes\",\"📦 Inicializar aprobados y sumaGeneral\",\"🔁 Repetir por cada estudiante\",\"📥 Leer tres notas\",\"➕ Calcular promedio\",\"🔀 Si promedio >= 12 incrementar aprobados\",\"➕ Acumular promedio general\",\"📤 Mostrar aprobados y promedio general\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8,9]",
                "[\"Combina entrada, proceso, decisión y repetición.\",\"La nota mínima aprobatoria indicada en el sílabo es 12.\",\"Divide la suma general entre la cantidad de estudiantes.\"]",
                "public class EvaluacionIntegradoraPro { public static void main(String[] args) { int aprobados=0; double suma=0; for(int i=0;i<cantidad;i++){ double promedio=(n1+n2+n3)/3.0; if(promedio>=12) aprobados++; suma+=promedio; } } }");

        ensurePremiumChallenge("Sistema de inventario Pro", "Pro", "POO + Colecciones",
                "Modela productos, controla stock y genera alertas usando clases y ArrayList.",
                "[\"▶ Inicio\",\"📦 Crear clase Producto\",\"📚 Crear ArrayList\",\"📥 Registrar productos\",\"🔁 Recorrer inventario\",\"🔀 Validar stock mínimo\",\"📤 Mostrar alertas\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7]",
                "[\"Define primero el modelo Producto.\",\"Usa una colección para almacenar objetos.\",\"Compara stock con el mínimo.\"]",
                "import java.util.*; class Producto { String nombre; int stock; } public class InventarioPro { public static void main(String[] args) { List<Producto> productos = new ArrayList<>(); } }");

        ensurePremiumChallenge("Matriz de calificaciones", "Pro", "Matrices",
                "Procesa notas de varios estudiantes, calcula promedios y encuentra la nota más alta.",
                "[\"▶ Inicio\",\"📦 Declarar matriz de notas\",\"🔁 Recorrer estudiantes\",\"🔁 Recorrer evaluaciones\",\"➕ Acumular notas\",\"➗ Calcular promedio\",\"🔀 Buscar mayor\",\"📤 Mostrar reporte\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8]",
                "[\"Usa bucles anidados.\",\"Calcula un promedio por fila.\",\"Actualiza la nota mayor al recorrer.\"]",
                "public class MatrizNotasPro { public static void main(String[] args) { double[][] notas = new double[5][4]; } }");

        ensurePremiumChallenge("Login con tres intentos", "Pro", "Seguridad lógica",
                "Valida usuario y contraseña permitiendo como máximo tres intentos.",
                "[\"▶ Inicio\",\"📦 intentos = 0\",\"🔁 Mientras intentos < 3\",\"📥 Leer credenciales\",\"🔀 Validar usuario y clave\",\"📤 Acceso permitido\",\"➕ Incrementar intentos\",\"📤 Cuenta bloqueada\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8]",
                "[\"Usa un while.\",\"Detén el ciclo si las credenciales son válidas.\",\"Bloquea al llegar a tres intentos.\"]",
                "public class LoginPro { public static void main(String[] args) { int intentos=0; while(intentos<3){ intentos++; } } }");

        ensurePremiumChallenge("Cajero automático", "Pro", "Métodos + Switch",
                "Implementa consultar saldo, depositar y retirar con validaciones.",
                "[\"▶ Inicio\",\"📦 Inicializar saldo\",\"📋 Mostrar menú\",\"📥 Leer opción\",\"🔀 Procesar con switch\",\"➕ Depositar\",\"➖ Retirar validando saldo\",\"📤 Mostrar saldo\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6,7,8]",
                "[\"Separa cada operación en un método.\",\"No permitas retirar más que el saldo.\",\"Usa switch para el menú.\"]",
                "public class CajeroPro { static double retirar(double saldo,double monto){ return monto<=saldo ? saldo-monto : saldo; } }");

        ensurePremiumChallenge("Ranking de estudiantes", "Pro", "Objetos + Ordenamiento",
                "Ordena estudiantes por puntaje y muestra los tres primeros lugares.",
                "[\"▶ Inicio\",\"📦 Crear clase Estudiante\",\"📚 Cargar lista\",\"🔁 Ordenar por puntaje\",\"🥇 Seleccionar top 3\",\"📤 Mostrar ranking\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Cada estudiante debe tener nombre y puntaje.\",\"Ordena de mayor a menor.\",\"Limita la salida a tres registros.\"]",
                "import java.util.*; public class RankingPro { public static void main(String[] args) { /* Comparator por puntaje */ } }");

        ensurePremiumChallenge("Recursividad: factorial", "Pro", "Recursividad",
                "Resuelve el factorial mediante una función recursiva con caso base.",
                "[\"▶ Inicio\",\"📥 Leer N\",\"📦 Declarar factorial(n)\",\"🔀 Si n <= 1 retornar 1\",\"↩ Retornar n * factorial(n-1)\",\"📤 Mostrar resultado\",\"⏹ Fin\"]",
                "[0,1,2,3,4,5,6]",
                "[\"Toda recursión necesita un caso base.\",\"Reduce n en cada llamada.\",\"Multiplica al retornar.\"]",
                "public class FactorialRecursivoPro { static long factorial(int n){ return n<=1 ? 1 : n*factorial(n-1); } }");
    }

    private void ensurePremiumChallenge(String titulo, String dificultad, String categoria, String descripcion,
                                        String bloquesJson, String solucionJson, String pistasJson, String codigoJava) {
        if (!challengeRepository.existsByTitulo(titulo)) {
            challengeRepository.save(Challenge.builder()
                    .titulo(titulo).dificultad(dificultad).categoria(categoria).descripcion(descripcion)
                    .bloquesJson(bloquesJson).solucionJson(solucionJson).pistasJson(pistasJson)
                    .codigoJava(codigoJava).premium(true).build());
        }
    }

    private void ensureUser(String nombre, String email, String rawPassword, Role rol) {
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(AppUser.builder()
                    .nombre(nombre)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .rol(rol)
                    .activo(true)
                    .build());
        }
    }

    private void ensureChallenge(String titulo, String dificultad, String categoria, String descripcion,
                                 String bloquesJson, String solucionJson, String pistasJson, String codigoJava) {
        if (!challengeRepository.existsByTitulo(titulo)) {
            challengeRepository.save(Challenge.builder()
                    .titulo(titulo)
                    .dificultad(dificultad)
                    .categoria(categoria)
                    .descripcion(descripcion)
                    .bloquesJson(bloquesJson)
                    .solucionJson(solucionJson)
                    .pistasJson(pistasJson)
                    .codigoJava(codigoJava)
                    .build());
        }
    }
}
