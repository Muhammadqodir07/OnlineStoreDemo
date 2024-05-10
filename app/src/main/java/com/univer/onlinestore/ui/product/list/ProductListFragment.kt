package com.univer.onlinestore.ui.product.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univer.onlinestore.MainActivity
import com.univer.onlinestore.R
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductFilter
import com.univer.onlinestore.databinding.FragmentProductListBinding
import com.univer.onlinestore.ui.product.list.filter.FilterDialogListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment(), FilterDialogListener {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFloatingActionButton()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).productListViewModel

        viewModel.products.observe(viewLifecycleOwner) { productList ->
            (binding.list.adapter as? ProductListAdapter)?.submitList(productList)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchProducts()
    }

    private fun setupRecyclerView() {
        binding.list.layoutManager = LinearLayoutManager(context)
        val adapter = ProductListAdapter(
            onClick = { product ->
                navigateToProductDetail(product)
            },
            onAddToCartClick = { _ ->
                Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
            }
        )
        binding.list.adapter = adapter
    }

    private fun setupFloatingActionButton() {
        binding.fabAddProduct.setOnClickListener {
            navigateToProductDetail(null)
        }
    }

    private fun navigateToProductDetail(product: Product?) {
        val bundle = Bundle().apply {
            product?.id?.let { putInt("productId", it) }
        }
        findNavController().navigate(
            R.id.action_productListFragment_to_productDetailFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFiltersApplied(filter: ProductFilter) {
        viewModel.applyFilter(filter)
    }
}
