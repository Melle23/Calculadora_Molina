package molina.esmeralda.calculadora_245201

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private var resultado: Double = 0.0
    private var operacionPendiente: String = ""
    private var nuevoNumero: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView.text = "0"

        val botonesNumeros = listOf(
            R.id.num0 to "0", R.id.num1 to "1", R.id.num2 to "2", R.id.num3 to "3", R.id.num4 to "4",
            R.id.num5 to "5", R.id.num6 to "6", R.id.num7 to "7", R.id.num8 to "8", R.id.num9 to "9"
        )

        botonesNumeros.forEach { (id, num) ->
            findViewById<Button>(id).setOnClickListener { numeroPresionado(num) }
        }

        findViewById<Button>(R.id.btnSuma).setOnClickListener { operadorPresionado("+") }
        findViewById<Button>(R.id.btnResta).setOnClickListener { operadorPresionado("-") }
        findViewById<Button>(R.id.btnMultiplicar).setOnClickListener { operadorPresionado("×") }
        findViewById<Button>(R.id.btnDividir).setOnClickListener { operadorPresionado("÷") }
        findViewById<Button>(R.id.btnIgual).setOnClickListener { calcularResultado() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { limpiarCalculadora() }
    }

    private fun numeroPresionado(numero: String) {
        if (nuevoNumero || textView.text == "0") {
            textView.text = numero
        } else {
            textView.append(numero)
        }
        nuevoNumero = false
    }

    private fun operadorPresionado(operador: String) {
        if (textView.text.isEmpty()) return

        val numeroActual = textView.text.toString().toDoubleOrNull()
        if (numeroActual != null) {
            if (operacionPendiente.isNotEmpty()) {
                calcularResultado()
            } else {
                resultado = numeroActual
            }
            operacionPendiente = operador
            textView.text = "$resultado $operador "
            nuevoNumero = true
        }
    }

    private fun calcularResultado() {
        if (operacionPendiente.isEmpty() || textView.text.isEmpty()) return

        val textoIngresado = textView.text.toString().split(" ").lastOrNull()
        val segundoNumero = textoIngresado?.toDoubleOrNull()

        if (segundoNumero != null) {
            resultado = when (operacionPendiente) {
                "+" -> resultado + segundoNumero
                "-" -> resultado - segundoNumero
                "×" -> resultado * segundoNumero
                "/" -> if (segundoNumero != 0.0) resultado / segundoNumero else return mostrarError("División por 0")
                else -> segundoNumero
            }
            textView.text = formatearNumero(resultado)
            operacionPendiente = ""
            nuevoNumero = true
        }
    }

    private fun limpiarCalculadora() {
        resultado = 0.0
        operacionPendiente = ""
        nuevoNumero = true
        textView.text = "0"
    }

    private fun mostrarError(mensaje: String) {
        textView.text = mensaje
        nuevoNumero = true
    }

    private fun formatearNumero(numero: Double): String {
        return if (numero % 1 == 0.0) numero.toInt().toString() else String.format("%.2f", numero)
    }
}