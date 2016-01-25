package com.androprogrammer.socialsignin.listeners

interface DialogButtonClickListener {

    fun onPositiveButtonClicked(dialogIdentifier: Int)
    fun onNegativeButtonClicked(dialogIdentifier: Int)
}
