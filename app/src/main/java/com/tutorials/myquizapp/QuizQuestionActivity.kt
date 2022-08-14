package com.tutorials.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener{

   private var mCurrentPosition:Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val tvOptionOne = findViewById<TextView>(R.id.tvOptionOne)
        val tvOptionTwo = findViewById<TextView>(R.id.tvOptionTwo)
        val tvOptionThree = findViewById<TextView>(R.id.tvOptionThree)
        val tvOptionFour = findViewById<TextView>(R.id.tvOptionFour)
        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

    }

    private fun setQuestion(){

        val question = mQuestionsList!![mCurrentPosition -1]

        defaultOptionsView()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        if (mCurrentPosition == mQuestionsList!!.size){
            btnSubmit.text = "FINISH"
        }else{
            btnSubmit.text = "SUBMIT"
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = mCurrentPosition

        val tvProgress = findViewById<TextView>(R.id.tvProgress)
        tvProgress.text = "$mCurrentPosition" + "/" + progressBar.max

        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        tvQuestion.text = question!!.question

        val ivImage = findViewById<ImageView>(R.id.ivImage)
        ivImage.setImageResource(question.image)

        val tvOptionOne = findViewById<TextView>(R.id.tvOptionOne)
        tvOptionOne.text = question.optionOne

        val tvOptionTwo = findViewById<TextView>(R.id.tvOptionTwo)
        tvOptionTwo.text = question.optionTwo

        val tvOptionThree = findViewById<TextView>(R.id.tvOptionThree)
        tvOptionThree.text = question.optionThree

        val tvOptionFour = findViewById<TextView>(R.id.tvOptionFour)
        tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val tvOptionOne = findViewById<TextView>(R.id.tvOptionOne)
        val tvOptionTwo = findViewById<TextView>(R.id.tvOptionTwo)
        val tvOptionThree = findViewById<TextView>(R.id.tvOptionThree)
        val tvOptionFour = findViewById<TextView>(R.id.tvOptionFour)
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )

        }
    }

    override fun onClick(p0: View?) {
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val tvOptionOne = findViewById<TextView>(R.id.tvOptionOne)
        val tvOptionTwo = findViewById<TextView>(R.id.tvOptionTwo)
        val tvOptionThree = findViewById<TextView>(R.id.tvOptionThree)
        val tvOptionFour = findViewById<TextView>(R.id.tvOptionFour)
        when(p0?.id){
            R.id.tvOptionOne ->{
                selectedOptionView(tvOptionOne, 1)
            }
            R.id.tvOptionTwo ->{
                selectedOptionView(tvOptionTwo, 2)
            }
            R.id.tvOptionThree ->{
                selectedOptionView(tvOptionThree, 3)
            }
            R.id.tvOptionFour ->{
                selectedOptionView(tvOptionFour, 4)
            }
            R.id.btnSubmit ->{
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition ++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }else ->{
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition -1)
                    if (question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size){
                        btnSubmit.text = "FINISH"
                    }else{
                        btnSubmit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0

                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        val tvOptionOne = findViewById<TextView>(R.id.tvOptionOne)
        val tvOptionTwo = findViewById<TextView>(R.id.tvOptionTwo)
        val tvOptionThree = findViewById<TextView>(R.id.tvOptionThree)
        val tvOptionFour = findViewById<TextView>(R.id.tvOptionFour)
        when(answer){
            1 ->{
                tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 ->{
                tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 ->{
                tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 ->{
                tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView,
                                   selectedOptionNum: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#7A8089"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

}