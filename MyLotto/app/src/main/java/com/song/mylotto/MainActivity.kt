package com.song.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6),
        )
    }

    private var didRun = false
    private val pickNumberset = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initAddButton()
        initRunButton()
        initCliearButton()
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                didRun -> showToast("초기화 후에 시도해주세요7.")
                pickNumberset.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumberset.contains(numPick.value) -> showToast("이미 선택된 숫자입니다.")
                else -> {
                    val textView = numTextViewList[pickNumberset.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()
                    setNumBerBack(numPick.value, textView)
                    pickNumberset.add(numPick.value)
                }
            }
        }
    }

    private fun initCliearButton() {

        clearButton.setOnClickListener {
            pickNumberset.clear()
            numTextViewList.forEach { it.isVisible=false }
            didRun = false
            numPick.value = 1
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandom()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBerBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumberset }
        return (pickNumberset + numbers.shuffled().take(6-pickNumberset.size)).sorted()
    }

    private fun setNumBerBack(number: Int, textView: TextView) {
        val background = when(number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

