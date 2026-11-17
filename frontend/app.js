// ShoppingHub Shared Configuration, State & Catalog
const API_BASE = "http://localhost:8080";

const Auth = {
    getToken() {
        return localStorage.getItem("jwt_token") || "";
    },
    getUser() {
        const u = localStorage.getItem("user_info");
        return u ? JSON.parse(u) : null;
    },
    getCustomerId() {
        return localStorage.getItem("customer_id") || "1";
    },
    setSession(token, username, customerId = "1") {
        localStorage.setItem("jwt_token", token);
        localStorage.setItem("user_info", JSON.stringify({ username }));
        localStorage.setItem("customer_id", customerId);
    },
    logout() {
        localStorage.removeItem("jwt_token");
        localStorage.removeItem("user_info");
        localStorage.removeItem("customer_id");
        localStorage.removeItem("shopping_cart");
        window.location.href = "login.html";
    }
};

const Cart = {
    get() {
        const c = localStorage.getItem("shopping_cart");
        return c ? JSON.parse(c) : [];
    },
    save(cart) {
        localStorage.setItem("shopping_cart", JSON.stringify(cart));
        window.dispatchEvent(new Event("cart_updated"));
    },
    add(product, qty = 1) {
        let items = this.get();
        const existing = items.find(item => item.id === product.id);
        if (existing) {
            existing.quantity += qty;
        } else {
            items.push({
                id: product.id,
                name: product.name,
                price: product.price,
                image: product.image,
                category: product.category,
                quantity: qty
            });
        }
        this.save(items);
    },
    remove(id) {
        let items = this.get().filter(item => item.id !== id);
        this.save(items);
    },
    updateQuantity(id, delta) {
        let items = this.get();
        const item = items.find(i => i.id === id);
        if (item) {
            item.quantity += delta;
            if (item.quantity <= 0) {
                items = items.filter(i => i.id !== id);
            }
            this.save(items);
        }
    },
    clear() {
        this.save([]);
    },
    totalCount() {
        return this.get().reduce((acc, item) => acc + item.quantity, 0);
    },
    totalAmount() {
        return this.get().reduce((acc, item) => acc + (item.price * item.quantity), 0);
    }
};

// Rich Indian E-Commerce Product Catalog across Shoes, Electronics, Snacks, Groceries, Men's Lifestyle & Gadgets
const PRODUCTS = [
    // === SHOES ===
    {
        id: "shoe-1",
        name: "Nike Air Zoom Pegasus 40 Running Shoes",
        category: "Shoes",
        price: 9995,
        rating: 4.8,
        reviews: 1420,
        badge: "Bestseller",
        image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=700&q=80",
        description: "Responsive cushioning with React foam and Zoom Air units for daily runs and marathon training."
    },
    {
        id: "shoe-2",
        name: "Adidas Originals Superstar Leather Sneakers",
        category: "Shoes",
        price: 7599,
        rating: 4.7,
        reviews: 950,
        badge: "Classic",
        image: "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=700&q=80",
        description: "Iconic shell-toe classic streetwear sneaker with smooth full-grain leather upper and rubber outsole."
    },
    {
        id: "shoe-3",
        name: "Puma Nitro Velocity 2 Sports Shoes",
        category: "Shoes",
        price: 5499,
        rating: 4.6,
        reviews: 430,
        badge: "Hot Deal",
        image: "https://images.unsplash.com/photo-1608231387042-66d1773070a5?auto=format&fit=crop&w=700&q=80",
        description: "Lightweight, nitrogen-infused foam midsole offering high energy rebound and grip on roads."
    },

    // === ELECTRONICS & GADGETS ===
    {
        id: "elec-1",
        name: "Apple MacBook Air 15\" (M3 Chip, 16GB, 512GB)",
        category: "Electronics",
        price: 134900,
        rating: 4.9,
        reviews: 820,
        badge: "Apple Official",
        image: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=700&q=80",
        description: "Strikingly thin design with Liquid Retina display, 18-hour battery, and blazing M3 processing."
    },
    {
        id: "elec-2",
        name: "Sony WH-1000XM5 Active Noise Cancelling Headphones",
        category: "Electronics",
        price: 28990,
        rating: 4.8,
        reviews: 2150,
        badge: "Top Rated",
        image: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=700&q=80",
        description: "Industry-leading noise cancellation, crystal clear hands-free calling, and up to 30 hours playback."
    },
    {
        id: "elec-3",
        name: "Samsung Galaxy S24 Ultra 5G (Titanium Gray)",
        category: "Electronics",
        price: 129999,
        rating: 4.9,
        reviews: 1680,
        badge: "Flagship",
        image: "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?auto=format&fit=crop&w=700&q=80",
        description: "Galaxy AI features, embedded S-Pen, titanium shield, and 200MP Quad Telephoto camera."
    },
    {
        id: "elec-4",
        name: "Apple Watch Series 9 GPS 45mm Midnight",
        category: "Gadgets",
        price: 44900,
        rating: 4.8,
        reviews: 740,
        badge: "Popular",
        image: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=700&q=80",
        description: "Advanced health sensors, ECG app, blood oxygen tracking, and innovative double-tap gesture control."
    },
    {
        id: "elec-5",
        name: "Logitech MX Master 3S Ergonomic Wireless Mouse",
        category: "Gadgets",
        price: 8995,
        rating: 4.9,
        reviews: 3100,
        badge: "Productivity",
        image: "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=700&q=80",
        description: "Quiet-click electromagnetic MagSpeed scroll wheel, 8,000 DPI track-on-glass sensor."
    },

    // === MEN'S LIFESTYLE & CLOTHING ===
    {
        id: "men-1",
        name: "Raymond Premium Tailored Fit Oxford Shirt",
        category: "Men",
        price: 2499,
        rating: 4.6,
        reviews: 412,
        badge: "Trending",
        image: "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=700&q=80",
        description: "100% long-staple Egyptian cotton with wrinkle-resistant finish and structured button-down collar."
    },
    {
        id: "men-2",
        name: "Levi's 511 Slim Fit Stretch Denim Jeans",
        category: "Men",
        price: 3699,
        rating: 4.7,
        reviews: 1850,
        badge: "Essential",
        image: "https://images.unsplash.com/photo-1542272604-780c96856465?auto=format&fit=crop&w=700&q=80",
        description: "Modern slim silhouette with added room for comfort and durable stretch cotton denim."
    },
    {
        id: "men-3",
        name: "Fossil Grant Chronograph Leather Watch for Men",
        category: "Men",
        price: 11495,
        rating: 4.8,
        reviews: 890,
        badge: "Luxury",
        image: "https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=700&q=80",
        description: "Roman numeral navy dial, stainless steel case, and genuine rich brown leather strap."
    },

    // === SNACKS & GROCERIES ===
    {
        id: "snack-1",
        name: "Happilo Premium California Roasted & Salted Almonds (500g)",
        category: "Snacks",
        price: 499,
        rating: 4.7,
        reviews: 2600,
        badge: "Healthy",
        image: "https://images.unsplash.com/photo-1508061252445-5350f3ab0a55?auto=format&fit=crop&w=700&q=80",
        description: "Crunchy, non-GMO, gluten-free dry-roasted whole almonds packed with healthy fats and protein."
    },
    {
        id: "snack-2",
        name: "Ferrero Rocher Gourmet Hazelnut Chocolates (24 Pcs)",
        category: "Snacks",
        price: 949,
        rating: 4.9,
        reviews: 3820,
        badge: "Celebration",
        image: "https://images.unsplash.com/photo-1549007994-cb92caebd54b?auto=format&fit=crop&w=700&q=80",
        description: "Crispy wafer shell enveloped in milk chocolate and hazelnut pieces with a whole hazelnut center."
    },
    {
        id: "groc-1",
        name: "Organic Tattva Extra Virgin Cold-Pressed Olive Oil (1L)",
        category: "Groceries",
        price: 1199,
        rating: 4.8,
        reviews: 640,
        badge: "Organic",
        image: "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=700&q=80",
        description: "First cold pressed from sun-ripened olives, rich in antioxidants and heart-healthy polyphenols."
    },
    {
        id: "groc-2",
        name: "Twinings English Breakfast & Green Tea Gift Chest",
        category: "Groceries",
        price: 899,
        rating: 4.7,
        reviews: 512,
        badge: "Specialty",
        image: "https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=700&q=80",
        description: "Curated selection of handpicked aromatic black, herbal, and pure green tea blends."
    }
];

// Helper to format currency in Indian Rupees
function formatINR(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(amount);
}

// Toast notification
function showToast(message, type = "info") {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        container.style.position = "fixed";
        container.style.bottom = "24px";
        container.style.right = "24px";
        container.style.zIndex = "99999";
        container.style.display = "flex";
        container.style.flexDirection = "column";
        container.style.gap = "10px";
        document.body.appendChild(container);
    }
    const toast = document.createElement("div");
    const bgStyles = {
        success: "bg-emerald-600 text-white",
        error: "bg-rose-600 text-white",
        info: "bg-slate-900 text-white"
    };
    const styleClass = bgStyles[type] || bgStyles.info;
    toast.className = `${styleClass} px-5 py-3.5 rounded-xl shadow-xl text-sm font-medium flex items-center gap-3 transform transition-all duration-300 opacity-0 translate-y-3`;
    toast.innerHTML = `<span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.remove("opacity-0", "translate-y-3");
    }, 10);
    setTimeout(() => {
        toast.classList.add("opacity-0", "translate-y-3");
        setTimeout(() => toast.remove(), 350);
    }, 3200);
}

