package com.univer.onlinestore.ui.product.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univer.onlinestore.byteArrayToBitmap
import com.univer.onlinestore.data.model.Currency
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductCategory
import com.univer.onlinestore.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailViewModel by viewModels()
    private var image: ByteArray? = null

    private val getImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    binding.imageProduct.setImageURI(uri)
                    image = uriToByteArray(uri = uri, context = requireContext())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("productId")?.let {
            viewModel.loadProduct(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        getImage.launch(intent)
    }

    private fun uriToByteArray(uri: Uri, context: Context): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: IOException) {
            Log.e("ImageConversion", "Error converting URI to byte array", e)
            null
        }
    }

    private fun setupCategoryAutoComplete() {
        val categories = ProductCategory.values()
        val categoryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.spinnerCategory.setAdapter(categoryAdapter)
    }

    private fun setupCurrencyAutoComplete() {
        val currencies = Currency.values()
        val currencyAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currencies)
        binding.spinnerCurrency.setAdapter(currencyAdapter)
    }

    private fun observeViewModel() {
        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                populateProductDetails(it)
            }
        }
    }

    private fun populateProductDetails(product: Product) {
        with(binding) {
            editProductName.setText(product.name)
            editProductDescription.setText(product.description)
            editProductPrice.setText(product.price.toString())
            spinnerCategory.setText(product.category.name, false)
            spinnerCurrency.setText(product.currency.name, false)

            image = product.image

            val bitmap = byteArrayToBitmap(image)
            imageProduct.setImageBitmap(bitmap)
        }
    }

    private fun setupListeners() {
        binding.btnSaveProduct.setOnClickListener {
            saveProduct()
        }
        setupImageChangeListener()
        setupCategoryAutoComplete()
        setupCurrencyAutoComplete()
    }

    private fun setupImageChangeListener() {
        binding.imageProduct.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun saveProduct() {
        val name = binding.editProductName.text.toString()
        val description = binding.editProductDescription.text.toString()
        val price = binding.editProductPrice.text.toString().toDoubleOrNull() ?: 0.0
        val category = ProductCategory.valueOf(binding.spinnerCategory.text.toString())
        val currency = Currency.valueOf(binding.spinnerCurrency.text.toString())

        viewModel.saveProduct(name, description, price, image, category, currency)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001

        fun newInstance(product: Product?): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle()
            if (product != null) {
                args.putParcelable("product", product)
            }
            fragment.arguments = args
            return fragment
        }
    }
}