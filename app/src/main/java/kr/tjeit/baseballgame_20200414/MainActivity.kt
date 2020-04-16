package kr.tjeit.baseballgame_20200414

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kr.tjeit.baseballgame_20200414.adapters.ChatAdapter
import kr.tjeit.baseballgame_20200414.datas.Chat

class MainActivity : BaseActivity() {

//    문제 숫자 세자리가 담길 ArrayList
    val computerNumbers = ArrayList<Int>()

    val chatings = ArrayList<Chat>()
    var mChatAdapter:ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
        makeComputerNumber()
    }

//    컴퓨터가 문제 출제하는 함수
    fun makeComputerNumber() {

        for (i in 0..2) {

            while (true) {

//            1. 1~9의 숫자만 사용 => 랜덤으로 생성되어야함.
//            2. 이미 뽑힌 숫자는 사용하면 안됨. (중복허용 X)
//      1 <= Math.random()*9+1 < 10
                val tempNum = (Math.random()*9+1).toInt()

//        중복검사 => 컴퓨터 숫자 배열안에 이 임시번호가 같은게 있다면?
                var isDuplOk = true

                for (cpNum in computerNumbers) {
                    if (tempNum == cpNum) {
                        isDuplOk = false
                    }
                }

                if (isDuplOk) {
                    computerNumbers.add(tempNum)
                    break
                }
            }

        }

        for (num in computerNumbers) {
            Log.d("출제번호", num.toString())
        }


    }




    override fun setupEvents() {

        okBtn.setOnClickListener {

            if (inputEdt.text.toString().length != 3) {
                Toast.makeText(mContext, "세자리 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (inputEdt.text.toString().contains(" ")) {
                Toast.makeText(mContext, "숫자만 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            chatings.add(Chat(inputEdt.text.toString(), "USER"))
            mChatAdapter?.notifyDataSetChanged()
        }

    }

    fun checkStrikeAndBall(inputStr : String){

        val inputNumArr = ArrayList<Int>()

        inputNumArr.add(inputStr.toInt() / 100) //0번칸 381 -> 3.81 / 100 - 3
        inputNumArr.add(inputStr.toInt()/10%10) //1번칸 381 -> -> 381 / 10 % 10
        inputNumArr.add(inputStr.toInt() % 10) //2번칸 381 -> 381 % 10 - 1

//        inputNumArr / computerNumbers 를 비교 / 몇s 몇b
        var strikeCount = 0
        var ballCount = 0

//        사용자 입력배열을 다루는 index : i
        for (i in 0..2){
//            컴퓨터 입력값을 다루는 index : j
            for (j in 0..2){
//                숫자가같은지
                if (computerNumbers.get(j) == inputNumArr.get(i)){
//                    위치까지 -> 같으면 s++, 다르면 B++
                    if (i==j){
                        strikeCount++
                    }
                    else{
                        ballCount++

                    }

                }
            }
        }
//총 몇개의 s/b 인지 담기계 됨
        chatings.add(Chat("${strikeCount}S ${ballCount}B 입니다" ))
        mChatAdapter?.notifyDataSetChanged()



    }


    override fun setValues() {

        chatings.add(Chat("숫자 야구게임에 오신걸 환영합니다.", "COMPUTER"))
        chatings.add(Chat("세자리 숫자를 맞춰주세요.", "COMPUTER"))
        chatings.add(Chat("중복된 숫자는 없고, 0도 사용되지 않습니다.", "COMPUTER"))

        mChatAdapter = ChatAdapter(mContext, R.layout.chat_list_item, chatings)
        chatListView.adapter = mChatAdapter

    }

}
