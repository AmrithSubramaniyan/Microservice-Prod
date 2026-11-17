// Standard Commercial E-Commerce Configuration & Shared Helpers
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
        localStorage.removeItem("shop_cart");
        window.location.href = "login.html";
    }
};

const Cart = {
    get() {
        const c = localStorage.getItem("shop_cart");
        return c ? JSON.parse(c) : [];
    },
    save(cart) {
        localStorage.setItem("shop_cart", JSON.stringify(cart));
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

// Real-world Commercial Products
const PRODUCTS = [
    {
        id: "prod-1",
        name: "Apple MacBook Pro 16\" (M3 Pro)",
        category: "Laptops",
        price: 2499.00,
        rating: 4.9,
        reviews: 324,
        image: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=700&q=80",
        description: "16.2-inch Liquid Retina XDR display, 18GB Unified Memory, 512GB SSD storage."
    },
    {
        id: "prod-2",
        name: "Sony WH-1000XM5 Wireless Headphones",
        category: "Audio",
        price: 399.99,
        rating: 4.8,
        reviews: 890,
        image: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=700&q=80",
        description: "Industry-leading noise cancellation with dual processors and 8 microphones."
    },
    {
        id: "prod-3",
        name: "Apple iPhone 15 Pro Max 256GB",
        category: "Smartphones",
        price: 1199.00,
        rating: 4.9,
        reviews: 1420,
        image: "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?auto=format&fit=crop&w=700&q=80",
        description: "Strong and lightweight titanium design with 48MP main camera and A17 Pro chip."
    },
    {
        id: "prod-4",
        name: "Dell UltraSharp 27\" 4K USB-C Hub Monitor",
        category: "Monitors",
        price: 579.50,
        rating: 4.7,
        reviews: 215,
        image: "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=700&q=80",
        description: "IPS Black technology with 2000:1 contrast ratio, 98% DCI-P3 color coverage."
    },
    {
        id: "prod-5",
        name: "Logitech MX Master 3S Wireless Mouse",
        category: "Accessories",
        price: 99.99,
        rating: 4.9,
        reviews: 2840,
        image: "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=700&q=80",
        description: "Quiet clicks and 8K DPI track-on-glass sensor with ergonomic thumb rest."
    },
    {
        id: "prod-6",
        name: "Apple Watch Series 9 GPS 45mm",
        category: "Wearables",
        price: 429.00,
        rating: 4.8,
        reviews: 672,
        image: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=700&q=80",
        description: "S9 chip powering bright display and magical new double tap gesture."
    }
];

// Clean Toast Notification
function showToast(message, type = "info") {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        container.style.position = "fixed";
        container.style.bottom = "20px";
        container.style.right = "20px";
        container.style.zIndex = "99999";
        container.style.display = "flex";
        container.style.flexDirection = "column";
        container.style.gap = "8px";
        document.body.appendChild(container);
    }
    const toast = document.createElement("div");
    const bgStyles = {
        success: "bg-emerald-600 text-white",
        error: "bg-red-600 text-white",
        info: "bg-slate-900 text-white"
    };
    const styleClass = bgStyles[type] || bgStyles.info;
    toast.className = `${styleClass} px-4 py-3 rounded-lg shadow-lg text-sm font-medium flex items-center gap-2 transform transition-all duration-300 opacity-0 translate-y-2`;
    toast.innerHTML = `<span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.remove("opacity-0", "translate-y-2");
    }, 10);
    setTimeout(() => {
        toast.classList.add("opacity-0", "translate-y-2");
        setTimeout(() => toast.remove(), 300);
    }, 3200);
}
