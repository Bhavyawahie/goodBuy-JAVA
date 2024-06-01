package com.goodbuy.store.entity;

import com.goodbuy.store.entity.embeddable.PaymentResult;
import com.goodbuy.store.entity.embeddable.ShippingAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "address", column = @Column(name = "shippingAddress_address")),
			@AttributeOverride(name = "city", column = @Column(name = "shippingAddress_city")),
			@AttributeOverride(name = "state", column = @Column(name = "shippingAddress_state")),
			@AttributeOverride(name = "pincode", column = @Column(name = "shippingAddress_pincode"))
	})
	private ShippingAddress shippingAddress;

	@Column(nullable = false)
	private String paymentMethod;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "paymentResult_id")),
			@AttributeOverride(name = "emailAddress", column = @Column(name = "paymentResult_emailAddress")),
			@AttributeOverride(name = "status", column = @Column(name = "paymentResult_status")),
			@AttributeOverride(name = "updateTime", column = @Column(name = "paymentResult_updateTime"))
	})
	private PaymentResult paymentResult;

	@Column(nullable = false)
	private Double taxPrice = 0.0;

	@Column(nullable = false)
	private Double shippingPrice = 0.0;

	@Column(nullable = false)
	private Double totalPrice = 0.0;

	@Column(nullable = false)
	private Boolean isPaid = false;

	private Date paidAt;

	@Column(nullable = false)
	private Boolean isDelivered = false;

	private Date deliveredAt;
	@CreationTimestamp
	private Date createdAt;
}
