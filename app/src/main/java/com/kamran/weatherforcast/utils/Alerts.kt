/*
 * Creator: Kamran Noorinejad on 5/27/20 11:27 AM
 * Last modified: 5/27/20 11:27 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.utils

import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kamran.weatherforcast.core.App
import com.kamran.weatherforcast.ui.base.BaseActivity

/**
 * Created by Kamran Noorinejad on 5/27/2020 AD 11:26.
 * Edited by Kamran Noorinejad on 5/27/2020 AD 11:26.
 */
object Alerts {

    var alert: AlertView? = null/*
    private var flashbar: Flashbar? = null
    private var flashbarConfig: Flashbar.Builder? = null
    private var flashbarProgress: Flashbar? = null
    private var flashbarProgressConfig: Flashbar.Builder? = null*/

    private const val default_message = "Are You Sure?"
    private const val default_positive_button_text = "Yes"
    private const val default_negative_button_text = "No"

    fun showAlertDialogWithDefaultButton(
        title: String, msg: String = default_message,
        action: String = default_positive_button_text,
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.DIALOG)
        alert?.addAction(AlertAction(action, AlertActionStyle.DEFAULT) {
        })
        alert?.show(activity)
    }

}