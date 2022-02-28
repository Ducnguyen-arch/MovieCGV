package com.ducnn17.movieCGV

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import com.ducnn17.movieCGV.receiver.Constant

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener,
Preference.OnPreferenceChangeListener{
    private var mCategoryPreference    : ListPreference? = null
    private var mRatePreference        : SeekBarPreference? = null
    private var mReleaseYearPreference : EditTextPreference? = null
    private var mSortPreference        : ListPreference? = null

    override fun onResume(){
        super.onResume()
        preferenceManager.sharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop(){
        preferenceManager.sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_setting)
        mCategoryPreference = findPreference(Constant.PREFERENCE_CATEGORY_KEY)
        mRatePreference = findPreference(Constant.PREFERENCE_RATE_KEY)
        mReleaseYearPreference = findPreference(Constant.PREFERENCE_RELEASE_YEAR_KEY)
        mSortPreference = findPreference(Constant.PREFERENCE_SORT_KEY)

        val mSharePreference : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            activity!!)
        mCategoryPreference!!.summary = mSharePreference.getString(Constant.PREFERENCE_CATEGORY_KEY,"Popular Movie")
        mRatePreference!!.summary = mSharePreference.getInt(Constant.PREFERENCE_RATE_KEY,0).toString()
        mReleaseYearPreference!!.summary = mSharePreference.getString(Constant.PREFERENCE_RELEASE_YEAR_KEY,"None")
        mSortPreference!!.summary = mSharePreference.getString(Constant.PREFERENCE_SORT_KEY,"None")

        mCategoryPreference!!.onPreferenceChangeListener = this
        mRatePreference!!.onPreferenceChangeListener = this
        mReleaseYearPreference!!.onPreferenceChangeListener = this
        mSortPreference!!.onPreferenceChangeListener = this

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Constant.PREFERENCE_CATEGORY_KEY){
            if (sharedPreferences != null) {
                mCategoryPreference!!.summary = sharedPreferences.getString(Constant.PREFERENCE_CATEGORY_KEY,"Popular Movie")
            }
        }else if (key == Constant.PREFERENCE_RATE_KEY){
            if (sharedPreferences != null) {
                mRatePreference!!.summary = sharedPreferences.getInt(Constant.PREFERENCE_RATE_KEY,0).toString()
            }
        }else if (key == Constant.PREFERENCE_RELEASE_YEAR_KEY){
            if (sharedPreferences != null) {
                mReleaseYearPreference!!.summary = sharedPreferences.getString(Constant.PREFERENCE_RELEASE_YEAR_KEY,"None")
            }
        }else if (key == Constant.PREFERENCE_SORT_KEY){
            if (sharedPreferences != null) {
                mSortPreference!!.summary = sharedPreferences.getString(Constant.PREFERENCE_SORT_KEY,"None")
            }
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
       when(preference?.key){
           Constant.PREFERENCE_CATEGORY_KEY ->{
               mCategoryPreference?.summary = mCategoryPreference?.value
               return true
           }
           Constant.PREFERENCE_RATE_KEY ->{
               mRatePreference?.summary = mRatePreference?.value.toString()
               return true
           }
           Constant.PREFERENCE_RELEASE_YEAR_KEY ->{
               mReleaseYearPreference?.summary = mReleaseYearPreference?.text
               return true
           }
           Constant.PREFERENCE_SORT_KEY ->{
               mSortPreference?.summary = mSortPreference?.value
               return true
           }
       }
        return true
    }

}