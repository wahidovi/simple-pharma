package com.simplepharma.backend.controller;

import com.simplepharma.backend.constants.ResponseCode;
import com.simplepharma.backend.constants.WebConstants;
import com.simplepharma.backend.dto.Order;
import com.simplepharma.backend.dto.ProductDto;
import com.simplepharma.backend.dto.ServerDto;
import com.simplepharma.backend.dto.ViewOrderDto;
import com.simplepharma.backend.exception.OrderCustomException;
import com.simplepharma.backend.exception.ProductCustomException;
import com.simplepharma.backend.model.PlaceOrder;
import com.simplepharma.backend.model.Product;
import com.simplepharma.backend.repository.CartRepository;
import com.simplepharma.backend.repository.OrderRepository;
import com.simplepharma.backend.repository.ProductRepository;
import com.simplepharma.backend.service.MyUserDetailService;
import com.simplepharma.backend.util.Validator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private OrderRepository ordRepo;

	@Autowired
	private CartRepository cartRepo;


	@PostMapping("/addProduct")
	public ResponseEntity<ProductDto> addProduct(
			@RequestParam(name = WebConstants.PROD_FILE, required = false) MultipartFile prodImage,
			@RequestParam(name = WebConstants.PROD_DESC) String description,
			@RequestParam(name = WebConstants.PROD_PRICE) String price,
			@RequestParam(name = WebConstants.PROD_NAME) String productname,
			@RequestParam(name = WebConstants.PROD_QUANITY) String quantity) throws IOException {
		ProductDto resp = new ProductDto();
		if (Validator.isStringEmpty(productname) || Validator.isStringEmpty(description)
				|| Validator.isStringEmpty(price) || Validator.isStringEmpty(quantity)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<ProductDto>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				Product prod = new Product();
				prod.setDescription(description);
				prod.setPrice(Double.parseDouble(price));
				prod.setProductName(productname);
				prod.setQuantity(Integer.parseInt(quantity));
				if (prodImage != null) {
					prod.setProductImage(prodImage.getBytes());
				}
				prodRepo.save(prod);
				logger.info("Product added.");
//				jmsTemplate.convertAndSend("product_queue", prod);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.ADD_SUCCESS_MESSAGE);
				resp.setOblist(prodRepo.findAll());
			} catch (Exception e) {
				throw new ProductCustomException("Unable to save product details, please try again");
			}
		}
		return new ResponseEntity<ProductDto>(resp, HttpStatus.OK);
	}

	@PutMapping("/updateProducts")
	public ResponseEntity<ServerDto> updateProducts(
			@RequestParam(name = WebConstants.PROD_FILE, required = false) MultipartFile prodImage,
			@RequestParam(name = WebConstants.PROD_DESC) String description,
			@RequestParam(name = WebConstants.PROD_PRICE) String price,
			@RequestParam(name = WebConstants.PROD_NAME) String productname,
			@RequestParam(name = WebConstants.PROD_QUANITY) String quantity,
			@RequestParam(name = WebConstants.PROD_ID) String productid) throws IOException {
		ServerDto resp = new ServerDto();
		if (Validator.isStringEmpty(productname) || Validator.isStringEmpty(description)
				|| Validator.isStringEmpty(price) || Validator.isStringEmpty(quantity)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<ServerDto>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				if (prodImage != null) {
					Product prod = new Product(Integer.parseInt(productid), description, productname,
							Double.parseDouble(price), Integer.parseInt(quantity), prodImage.getBytes());
					prodRepo.save(prod);
					logger.info("Product Updated.");
				} else {
					Product prodOrg = prodRepo.findByProductId(Integer.parseInt(productid));
					Product prod = new Product(Integer.parseInt(productid), description, productname,
							Double.parseDouble(price), Integer.parseInt(quantity), prodOrg.getProductImage());
					prodRepo.save(prod);
				}
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.UPD_SUCCESS_MESSAGE);
			} catch (Exception e) {
				throw new ProductCustomException("Unable to update product details, please try again");
			}
		}
		return new ResponseEntity<ServerDto>(resp, HttpStatus.OK);
	}

	@DeleteMapping("/delProduct")
	public ResponseEntity<ProductDto> delProduct(@RequestParam(name = WebConstants.PROD_ID) String productid)
			throws IOException {
		ProductDto resp = new ProductDto();
		if (Validator.isStringEmpty(productid)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<ProductDto>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				prodRepo.deleteByProductId(Integer.parseInt(productid));
				logger.info("Product deleted.");
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DEL_SUCCESS_MESSAGE);
			} catch (Exception e) {
				throw new ProductCustomException("Unable to delete product details, please try again");
			}
		}
		return new ResponseEntity<ProductDto>(resp, HttpStatus.OK);
	}

	@GetMapping("/viewOrders")
	public ResponseEntity<ViewOrderDto> viewOrders() throws IOException {

		ViewOrderDto resp = new ViewOrderDto();
		try {
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.VIEW_SUCCESS_MESSAGE);
			List<Order> orderList = new ArrayList<>();
			List<PlaceOrder> poList = ordRepo.findAll();
			poList.forEach((po) -> {
				Order ord = new Order();
				ord.setOrderBy(po.getEmail());
				ord.setOrderId(po.getOrderId());
				ord.setOrderStatus(po.getOrderStatus());
				ord.setProducts(cartRepo.findAllByOrderId(po.getOrderId()));
				orderList.add(ord);
			});
			resp.setOrderlist(orderList);
		} catch (Exception e) {
			throw new OrderCustomException("Unable to retrieve orderss, please try again");
		}

		return new ResponseEntity<ViewOrderDto>(resp, HttpStatus.OK);
	}

	@PostMapping("/updateOrder")
	public ResponseEntity<ServerDto> updateOrders(@RequestParam(name = WebConstants.ORD_ID) String orderId,
												  @RequestParam(name = WebConstants.ORD_STATUS) String orderStatus) throws IOException {

		ServerDto resp = new ServerDto();
		if (Validator.isStringEmpty(orderId) || Validator.isStringEmpty(orderStatus)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<ServerDto>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				PlaceOrder pc = ordRepo.findByOrderId(Integer.parseInt(orderId));
				pc.setOrderStatus(orderStatus);
				pc.setOrderDate(new Date(System.currentTimeMillis()));
				ordRepo.save(pc);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.UPD_ORD_SUCCESS_MESSAGE);
			} catch (Exception e) {
				throw new OrderCustomException("Unable to retrieve orderss, please try again");
			}
		}
		return new ResponseEntity<ServerDto>(resp, HttpStatus.OK);
	}
}
