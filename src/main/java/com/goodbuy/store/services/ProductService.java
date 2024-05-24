package com.goodbuy.store.services;

import com.goodbuy.store.dao.ProductDAO;
import com.goodbuy.store.dao.ProductReviewDAO;
import com.goodbuy.store.dao.UserDAO;
import com.goodbuy.store.dto.ProductDTO;
import com.goodbuy.store.dto.ReviewDTO;
import com.goodbuy.store.dto.UserDTO;
import com.goodbuy.store.entity.Product;
import com.goodbuy.store.entity.Review;
import com.goodbuy.store.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ProductReviewDAO productReviewDAO;
	@Autowired
	private CloudinaryService cloudinaryService;

	public List<ProductDTO> getAllProducts() {
		return productDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByKeyword(String keyword) {
		return productDAO.findByNameContainingIgnoreCase(keyword).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByCategory(String category) {
		return productDAO.findByCategory(category).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProductDTO> getProductByCategoryAndKeyword(String category, String keyword) {
		return productDAO.findByCategoryAndKeywordContainingIgnoreCase(category,keyword).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Page<ProductDTO> getProductByPage(Pageable pageable) {
		return productDAO.findAll(pageable).map(this::convertToDTO);
	}

	public Page<ProductDTO> getFilteredProducts(String keyword, String category, Pageable pageable) {
		if (keyword != null && category != null) {
			return productDAO.findByCategoryAndNameContainingIgnoreCase(category, keyword, pageable).map(this::convertToDTO);
		} else if (keyword != null) {
			return productDAO.findByNameContainingIgnoreCase(keyword, pageable).map(this::convertToDTO);
		} else if (category != null) {
			return productDAO.findByCategory(category, pageable).map(this::convertToDTO);
		} else {
			return productDAO.findAll(pageable).map(this::convertToDTO);
		}
	}

	public ProductDTO addProduct(int userId) {
		User user = userDAO.findById((long) userId).get();
		Product product = new Product().builder()
				.user(user)
				.name("Sample name")
				.image("/images/sample.jpeg")
				.brand("ACME")
				.category("Sample category")
				.description("Sample description")
				.rating(0.0)
				.numReviews(0l)
				.price(0l)
				.countInStock(0l)
				.build();
		return convertToDTO(productDAO.save(product));
	}


	public Optional<ProductDTO> getProductById(long id) {
		return productDAO.findById(id).map(this::convertToDTO);
	}

	public Map<String, String> deleteProduct(long id) {
		productDAO.deleteById(id);
		return Map.of("message", "Product Deleted");
	}

	public Map<String, String> addProductReview(long id, long userId, ReviewDTO review) {
		int numberOfReviewsByAUser = productReviewDAO.findByProductIdAndUserId(id, userId).size();
		if (numberOfReviewsByAUser > 0) {
			return Map.of("message", "Product Review already exists");
		}
		Review newReview = Review.builder()
				.title(review.getTitle())
				.rating(review.getRating())
				.comment(review.getComment())
				.product(productDAO.findById(id).get())
				.user(userDAO.findById(userId).get())
				.build();

		productReviewDAO.save(newReview);
		return Map.of("message", "Product Review added");
	}

	public Map<String, String> uploadProductImage(MultipartFile image, long id) throws IOException {
		String uploadedFileURL = cloudinaryService.uploadFile(image, id);
		Product product = productDAO.findById(id).get();
		product.setImage(uploadedFileURL);
		productDAO.save(product);
		return Map.of("url" , uploadedFileURL);
	}

	private ProductDTO convertToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		User productUser = product.getUser();
		UserDTO userDTO = UserDTO.builder()
				._id(productUser.getId())
				.name(productUser.getName())
				.email(productUser.getEmail())
				.isAdmin(Objects.equals(productUser.getRole().toString(), "ADMIN"))
				.build();
		BeanUtils.copyProperties(product, productDTO);
		productDTO.setUser(userDTO);
		productDTO.set_id(product.getId());
		return productDTO;
	}

}
