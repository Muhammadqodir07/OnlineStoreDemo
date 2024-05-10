package com.univer.onlinestore.ui.product.list.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.univer.onlinestore.data.model.Currency
import com.univer.onlinestore.data.model.ProductCategory
import com.univer.onlinestore.data.model.ProductFilter
import com.univer.onlinestore.databinding.DialogFilterBinding
import com.univer.onlinestore.enumValueOfOrNull

class FilterDialogFragment : DialogFragment() {

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        setupDropdowns()
        return binding.root
    }

    private fun setupDropdowns() {
        with(binding) {
            val categories = ProductCategory.values()
            val currencies = Currency.values()

            val categoryAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                categories
            )
            spinnerFilterCategory.setAdapter(categoryAdapter)

            val currencyAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                currencies
            )
            spinnerFilterCurrency.setAdapter(currencyAdapter)

            btnApplyFilters.setOnClickListener {
                val filter = ProductFilter(
                    category = enumValueOfOrNull<ProductCategory>(spinnerFilterCategory.text.toString()),
                    currency = enumValueOfOrNull<Currency>(spinnerFilterCurrency.text.toString()),
                    minPrice = editFilterMinPrice.text.toString().toDoubleOrNull(),
                    maxPrice = editFilterMaxPrice.text.toString().toDoubleOrNull()
                )
                applyFilters(filter)
                dismiss()
            }

            btnClearFilters.setOnClickListener {
                clearFilters()
                dismiss()
            }
        }
    }

    private fun applyFilters(filter: ProductFilter) {
        (activity as? FilterDialogListener)?.onFiltersApplied(filter)
    }

    private fun clearFilters() {
        (activity as? FilterDialogListener)?.onFiltersApplied(ProductFilter())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FilterDialogFragment = FilterDialogFragment()
    }
}