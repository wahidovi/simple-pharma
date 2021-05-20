package com.simplepharma.backend.controller;

import com.simplepharma.backend.constants.ResponseCode;
import com.simplepharma.backend.constants.WebConstants;
import com.simplepharma.backend.dto.*;
import com.simplepharma.backend.exception.*;
import com.simplepharma.backend.model.*;
import com.simplepharma.backend.repository.*;
import com.simplepharma.backend.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addrRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private OrderRepository ordRepo;

	@PostMapping("/addAddress")
	public ResponseEntity<UserDto> addAddress(@RequestBody Address address, Authentication auth) {
		UserDto resp = new UserDto();
		if (Validator.isAddressEmpty(address)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			return new ResponseEntity<UserDto>(resp, HttpStatus.NOT_ACCEPTABLE);
		} else {
			try {
				User user = userRepo.findByUsername(auth.getName())
						.orElseThrow(() -> new UsernameNotFoundException(auth.getName()));
				Address userAddress = addrRepo.findByUser(user);
				if (userAddress != null) {
					userAddress.setAddress(address.getAddress());
					userAddress.setCity(address.getCity());
					userAddress.setCountry(address.getCountry());
					userAddress.setPhoneNumber(address.getPhoneNumber());
					userAddress.setState(address.getState());
					userAddress.setZipCode(address.getZipCode());
					addrRepo.save(userAddress);
				} else {
					user.setAddress(address);
					address.setUser(user);
					addrRepo.save(address);
				}
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_ADR_ADD);
			} catch (Exception e) {
				throw new AddressCustomException("Unable to add address, please try again");
			}
		}
		return new ResponseEntity<UserDto>(resp, HttpStatus.OK);
	}

	@GetMapping("/getAddress")
	public ResponseEntity<Response> getAddress(Authentication auth) {
		Response resp = new Response();
		try {
			User user = userRepo.findByUsername(auth.getName()).orElseThrow(
					() -> new UserCustomException("User with username " + auth.getName() + " doesn't exists"));
			Address adr = addrRepo.findByUser(user);

			HashMap<String, String> map = new HashMap<>();
			map.put(WebConstants.ADR_NAME, adr.getAddress());
			map.put(WebConstants.ADR_CITY, adr.getCity());
			map.put(WebConstants.ADR_STATE, adr.getState());
			map.put(WebConstants.ADR_COUNTRY, adr.getCountry());
			map.put(WebConstants.ADR_ZP, String.valueOf(adr.getZipCode()));
			map.put(WebConstants.PHONE, adr.getPhoneNumber());

			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.CUST_ADR_ADD);
			resp.setMap(map);
		} catch (Exception e) {
			throw new AddressCustomException("Unable to retrieve address, please try again");
		}
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}

	@GetMapping("/getProducts")
	public ResponseEntity<ProductDto> getProducts(Authentication auth) throws IOException {
		ProductDto resp = new ProductDto();
		try {
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);
			resp.setOblist(prodRepo.findAll());
		} catch (Exception e) {
			throw new ProductCustomException("Unable to retrieve products, please try again");
		}
		return new ResponseEntity<ProductDto>(resp, HttpStatus.OK);
	}

	@GetMapping("/addToCart")
	public ResponseEntity<ServerDto> addToCart(@RequestParam(WebConstants.PROD_ID) String productId,
											   Authentication auth) throws IOException {

		ServerDto resp = new ServerDto();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserCustomException(auth.getName()));
			Product cartItem = prodRepo.findByProductId(Integer.parseInt(productId));

			Cart buf = new Cart();
			buf.setEmail(loggedUser.getEmail());
			buf.setQuantity(1);
			buf.setPrice(cartItem.getPrice());
			buf.setProductId(Integer.parseInt(productId));
			buf.setProductname(cartItem.getProductName());
			Date date = new Date();
			buf.setDateAdded(date);

			cartRepo.save(buf);

			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.CART_UPD_MESSAGE_CODE);
		} catch (Exception e) {
			throw new CartCustomException("Unable to add product to cart, please try again");
		}
		return new ResponseEntity<ServerDto>(resp, HttpStatus.OK);
	}

	@GetMapping("/viewCart")
	public ResponseEntity<CartDto> viewCart(Authentication auth) throws IOException {
		logger.info("Inside View cart request method");
		CartDto resp = new CartDto();
		try {
			logger.info("Inside View cart request method 2");
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserCustomException(auth.getName()));
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.VW_CART_MESSAGE);
			resp.setOblist(cartRepo.findByEmail(loggedUser.getEmail()));
		} catch (Exception e) {
			throw new CartCustomException("Unable to retrieve cart items, please try again");
		}

		return new ResponseEntity<CartDto>(resp, HttpStatus.OK);
	}

	@PutMapping("/updateCart")
	public ResponseEntity<CartDto> updateCart(@RequestBody HashMap<String, String> cart, Authentication auth)
			throws IOException {

		CartDto resp = new CartDto();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserCustomException(auth.getName()));
			Cart selCart = cartRepo.findByCartIdAndEmail(Integer.parseInt(cart.get("id")), loggedUser.getEmail());
			selCart.setQuantity(Integer.parseInt(cart.get("quantity")));
			cartRepo.save(selCart);
			List<Cart> bufcartlist = cartRepo.findByEmail(loggedUser.getEmail());
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.UPD_CART_MESSAGE);
			resp.setOblist(bufcartlist);
		} catch (Exception e) {
			throw new CartCustomException("Unable to update cart items, please try again");
		}

		return new ResponseEntity<CartDto>(resp, HttpStatus.OK);
	}

	@DeleteMapping("/delCart")
	public ResponseEntity<CartDto> delCart(@RequestParam(name = WebConstants.BUF_ID) String bufcartid,
                                           Authentication auth) throws IOException {

		CartDto resp = new CartDto();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserCustomException(auth.getName()));
			cartRepo.deleteByCartIdAndEmail(Integer.parseInt(bufcartid), loggedUser.getEmail());
			List<Cart> bufcartlist = cartRepo.findByEmail(loggedUser.getEmail());
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.DEL_CART_SUCCESS_MESSAGE);
			resp.setOblist(bufcartlist);
		} catch (Exception e) {
			throw new CartCustomException("Unable to delete cart items, please try again");
		}
		return new ResponseEntity<CartDto>(resp, HttpStatus.OK);
	}

	@GetMapping("/placeOrder")
	public ResponseEntity<ServerDto> placeOrder(Authentication auth) throws IOException {

		ServerDto resp = new ServerDto();
		try {
			User loggedUser = userRepo.findByUsername(auth.getName())
					.orElseThrow(() -> new UserCustomException(auth.getName()));
			PlaceOrder po = new PlaceOrder();
			po.setEmail(loggedUser.getEmail());
			Date date = new Date();
			po.setOrderDate(date);
			po.setOrderStatus(ResponseCode.ORD_STATUS_CODE);
			double total = 0;
			List<Cart> buflist = cartRepo.findAllByEmail(loggedUser.getEmail());
			for (Cart buf : buflist) {
				total = +(buf.getQuantity() * buf.getPrice());
			}
			po.setTotalCost(total);
			PlaceOrder res = ordRepo.save(po);
			buflist.forEach(bufcart -> {
				bufcart.setOrderId(res.getOrderId());
				cartRepo.save(bufcart);

			});
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.ORD_SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new PlaceOrderCustomException("Unable to place order, please try again later");
		}
		return new ResponseEntity<ServerDto>(resp, HttpStatus.OK);
	}
}
