package com.app.coffeemanagementapplication;


import android.app.Application;

import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.RoleType;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.ICategoryRepo;
import com.app.coffeemanagementapplication.repositories.IDiscountRepo;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IPaymentRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.repositories.IUserRepo;
import com.app.coffeemanagementapplication.services.CategoryService;
import com.app.coffeemanagementapplication.services.DiscountService;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.PaymentService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.app.coffeemanagementapplication.services.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo SharePres
        MySharePrefers.init(this);
        // Khởi tạo repo
        ICategoryRepo categoryRepo = new CategoryService(this);
        IProductRepo productRepo = new ProductService(this);
        IUserRepo userRepo = new UserService(this);
        IPaymentRepo paymentRepo = new PaymentService(this);
        IDiscountRepo discountRepo = new DiscountService(this);
        IFeedbackRepo feedbackRepo = new FeedbackService(this);
        // Chỉ insert nếu DB trống (tránh nhân đôi)
        List<Category> existingCategories = categoryRepo.getAllCategories();
        if (existingCategories == null || existingCategories.isEmpty()) {
            seedCategories(categoryRepo);
        }

        List<Product> existingProducts = productRepo.getAllProducts();
        if (existingProducts == null || existingProducts.isEmpty()) {
            seedProducts(productRepo);
        }
        List<Users> existingUsers = userRepo.getAllUsers();
        if (existingUsers == null || existingUsers.isEmpty()) {
            seedUsers(userRepo);
        }
        List<Payment> paymentList = paymentRepo.getAllPaymentMethods();
        if (paymentList == null || paymentList.isEmpty()) {
            seedPayments(paymentRepo);
        }
        List<Discount> discountList = discountRepo.getAllDiscounts();
        if (discountList == null || discountList.isEmpty()) {
            seedDiscounts(discountRepo);
        }
        List<Feedback> feedbackList = feedbackRepo.getAllFeedbacks();
        if (feedbackList == null || feedbackList.isEmpty()) {
            seedFeedbacks(feedbackRepo);
        }
    }

    private void seedUsers(IUserRepo userRepo) {
        userRepo.insertUser(new Users(

                "Nguyễn Văn A",
                "vana@example.com",
                "123456",
                RoleType.CUSTOMER,
                "0987654321",
                "2025-10-28 10:00:00",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHfd3PPulVSp4ZbuBFNkePoUR_fLJQe474Ag&s"
                ,
                "2025-10-28 10:00:00"

        ));

        userRepo.insertUser(new Users(

                "Trần Thị B",
                "thib@example.com",
                "123456",
                RoleType.CUSTOMER,
                "0978123456",
                "2025-10-28 10:05:00",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHfd3PPulVSp4ZbuBFNkePoUR_fLJQe474Ag&s"
                ,
                "2025-10-28 10:05:00"
        ));

        userRepo.insertUser(new Users(

                "Admin",
                "admin@example.com",
                "admin123",
                RoleType.ADMIN,
                "0909000000",
                "2025-10-28 10:10:00",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHfd3PPulVSp4ZbuBFNkePoUR_fLJQe474Ag&s"
                ,
                "2025-10-28 10:10:00"
        ));
    }

    // Seed Category
    private void seedCategories(ICategoryRepo categoryRepo) {
        categoryRepo.insertCategory(new Category("Cafe", "Các loại cafe", "", ""));
        categoryRepo.insertCategory(new Category("Trà Sữa", "Trà sữa các vị", "", ""));
        categoryRepo.insertCategory(new Category("Sinh Tố", "Sinh tố trái cây", "", ""));
    }

    private void seedFeedbacks(IFeedbackRepo feedbackRepo) {
        Random random = new Random();

        int userId = 2;   // giả sử userId cố định (hoặc có thể random)
        int orderId = 1;  // tạm thời gán cố định để tránh lỗi ForeignKey

        for (int productId = 1; productId <= 3; productId++) {
            // Random số lượng feedback cho mỗi sản phẩm (từ 3 đến 8)
            int feedbackCount = random.nextInt(6) + 3;

            for (int i = 0; i < feedbackCount; i++) {
                int rating = random.nextInt(5) + 1; // random 1–5 sao

                String comment;
                switch (rating) {
                    case 5:
                        comment = "Tuyệt vời! Rất hài lòng ☕";
                        break;
                    case 4:
                        comment = "Khá ổn, mình thích!";
                        break;
                    case 3:
                        comment = "Ổn, có thể cải thiện thêm.";
                        break;
                    case 2:
                        comment = "Chưa được như mong đợi.";
                        break;
                    default:
                        comment = "Tệ, không hài lòng.";
                        break;
                }

                String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .format(new Date());

                Feedback feedback = new Feedback(
                        null,
                        userId,
                        productId,
                        rating,
                        comment,
                        createdAt
                );

                feedbackRepo.insertFeedback(feedback);
            }
        }
    }

    private void seedDiscounts(IDiscountRepo discountRepo) {
        discountRepo.insertDiscount(new Discount(
                "Giảm 10K cho đơn đầu tiên",
                "WELCOME10",
                "Áp dụng cho khách hàng mới, giảm 10.000đ cho đơn hàng đầu tiên.",
                50000,
                10000,
                "Dành cho đơn hàng từ 50.000đ",
                "2025-10-01",
                "2025-12-31",
                true, false
        ));

        discountRepo.insertDiscount(new Discount(
                "Giảm 20K cho đơn từ 100K",
                "SAVE20",
                "Giảm 20.000đ cho đơn hàng từ 100.000đ trở lên.",
                100000,
                20000,
                "Áp dụng cho mọi khách hàng",
                "2025-10-01",
                "2025-11-30",
                true, false
        ));

        discountRepo.insertDiscount(new Discount(
                "Ưu đãi sinh nhật",
                "HAPPYBD",
                "Giảm 30.000đ khi mua hàng trong tháng sinh nhật của bạn.",
                80000,
                30000,
                "Chỉ áp dụng trong tháng sinh nhật",
                "2025-01-01",
                "2025-12-31",
                true, false
        ));

        discountRepo.insertDiscount(new Discount(
                "Giảm 15K cho đơn cà phê buổi sáng",
                "MORNING15",
                "Áp dụng từ 6:00 - 10:00 sáng, giảm 15.000đ cho đơn từ 60.000đ.",
                60000,
                15000,
                "Uống cà phê sáng tiết kiệm hơn",
                "2025-10-01",
                "2025-12-31",
                true, false
        ));

        discountRepo.insertDiscount(new Discount(
                "Giảm 50K cho đơn trên 200K",
                "BIGORDER50",
                "Giảm 50.000đ cho đơn hàng từ 200.000đ trở lên.",
                200000,
                50000,
                "Áp dụng cho các đơn lớn",
                "2025-10-15",
                "2025-11-30",
                true, false
        ));
    }

    private void seedPayments(IPaymentRepo paymentRepo) {
        paymentRepo.insertPayment(new Payment(null, "Thanh toán tiền mặt", R.drawable.cash_payment_ic, "(Thanh toán khi nhận hàng)", false));
        paymentRepo.insertPayment(new Payment(null, "Credit or Debit Card", R.drawable.credit_cash_ic, "(Thẻ Visa hoặc Mastercard)", false));
        paymentRepo.insertPayment(new Payment(null, "Chuyển khoản ngân hàng", R.drawable.bank_transfer_ic, "(Tự động xác nhận)", false));
        paymentRepo.insertPayment(new Payment(null, "Zalo pay", R.drawable.zalo_pay_ic, "(Tự động xác nhận)", false));
    }

    // 🟫 Seed Product
    private void seedProducts(IProductRepo productRepo) {
        productRepo.insertProduct(new Product(1, 1, "Cà phê sữa đá", "Cà phê truyền thống Việt Nam", 25000,
                "https://cdn.pixabay.com/photo/2020/03/28/14/38/egg-coffee-4977310_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(2, 1, "Espresso", "Cà phê Ý đậm vị", 30000,
                "https://cdn.pixabay.com/photo/2022/11/07/04/58/dalgona-coffee-7575608_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(3, 2, "Trà sữa trân châu", "Thức uống ngọt ngào", 35000,
                "https://cdn.pixabay.com/photo/2020/03/05/12/44/orange-4904390_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(4, 3, "Sinh tố xoài", "Sinh tố tươi mát", 40000,
                "https://cdn.pixabay.com/photo/2020/02/03/07/18/drink-4814956_1280.jpg", true, "", ""));

    }
}
