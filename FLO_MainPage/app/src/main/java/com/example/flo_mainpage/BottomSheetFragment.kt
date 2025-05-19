package com.example.umc_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.flo_mainpage.R
import com.example.flo_mainpage.SongDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var deleteListener: (() -> Unit)? = null

    fun setOnDeleteListener(listener: () -> Unit) {
        deleteListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        val deleteBtn = view.findViewById<ImageView>(R.id.sheetBtn4)
        deleteBtn.setOnClickListener {
            val songDB = SongDatabase.getInstance(requireContext())!!

            Thread {
                songDB.songDao().deleteAll()

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "모든 노래가 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                    // 삭제 완료 콜백 호출
                    deleteListener?.invoke()

                    dismiss()
                }
            }.start()
        }

        return view
    }
}