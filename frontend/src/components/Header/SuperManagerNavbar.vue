<template>
  <header class="farm-navbar">
    <div class="farm-navbar-container">
      <a href="/" class="farm-navbar-brand">
        <div class="farm-navbar-logo">
          <svg
            width="28"
            height="28"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M12 2C13.5 2 15 3 15 5C15 6 14.5 7 14 8C16 9 17 10 17 12C17 14 15 15 15 17C15 19 13.5 20 12 22C10.5 20 9 19 9 17C9 15 10 14 10 12C10 10 11 9 13 8C12.5 7 12 6 12 5C12 3 10.5 2 12 2Z"
              fill="#22C55E"
            />
            <path
              d="M3 12C4 10 6 9 8 9C9 9 10 9.5 11 10C10 11 9 13 9 15C9 17 10 19 12 20V22H10C8 22 6 21 5 19C4 17 4 15 5 13C5 12 5 11 6 10C5 11 4 11 3 12Z"
              fill="#15803D"
            />
            <path
              d="M21 12C20 10 18 9 16 9C15 9 14 9.5 13 10C14 11 15 13 15 15C15 17 14 19 12 20V22H14C16 22 18 21 19 19C20 17 20 15 19 13C19 12 19 11 18 10C19 11 20 11 21 12Z"
              fill="#15803D"
            />
          </svg>
        </div>
        <span class="farm-navbar-title">农场</span>
      </a>

      <nav class="farm-navbar-nav">
        <a href="staffShow" class="farm-navbar-link">
          <svg
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
          </svg>
          员工管理
        </a>
        <a href="attendance" class="farm-navbar-link">
          <svg
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
            <line x1="16" y1="2" x2="16" y2="6"></line>
            <line x1="8" y1="2" x2="8" y2="6"></line>
            <line x1="3" y1="10" x2="21" y2="10"></line>
          </svg>
          考勤管理
        </a>
        <a href="reportForm" class="farm-navbar-link">
          <svg
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <polyline points="6 9 6 2 18 2 18 9"></polyline>
            <path
              d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"
            ></path>
            <rect x="6" y="14" width="12" height="8"></rect>
          </svg>
          报表打印
        </a>

        <div class="farm-navbar-dropdown">
          <button class="farm-navbar-dropdown-trigger">
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
            <span>{{ username }}</span>
            <svg
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <polyline points="6 9 12 15 18 9"></polyline>
            </svg>
          </button>
          <div class="farm-navbar-dropdown-menu">
            <a href="passwordSupermanager" class="farm-navbar-dropdown-item">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
              </svg>
              修改密码
            </a>
            <div class="farm-navbar-dropdown-divider"></div>
            <a
              @click="loginOut"
              class="farm-navbar-dropdown-item farm-navbar-dropdown-item-danger"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                <polyline points="16 17 21 12 16 7"></polyline>
                <line x1="21" y1="12" x2="9" y2="12"></line>
              </svg>
              退出登录
            </a>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script>
export default {
  name: "SupermanagerNavbar",
  data: () => ({
    username: sessionStorage.getItem("username")
  }),
  methods: {
    loginOut: function() {
      this.$router.replace("/login");
    }
  }
};
</script>

<style scoped>
.farm-navbar {
  background: linear-gradient(
    135deg,
    var(--farm-primary) 0%,
    var(--farm-primary-dark) 100%
  );
  box-shadow: var(--farm-shadow);
  position: sticky;
  top: 0;
  z-index: 100;
}

.farm-navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.farm-navbar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: white;
}

.farm-navbar-logo {
  width: 44px;
  height: 44px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--farm-transition);
}

.farm-navbar-brand:hover .farm-navbar-logo {
  background: rgba(255, 255, 255, 0.25);
  transform: scale(1.05);
}

.farm-navbar-title {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.farm-navbar-nav {
  display: flex;
  align-items: center;
  gap: 8px;
}

.farm-navbar-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--farm-radius);
  transition: var(--farm-transition);
}

.farm-navbar-link:hover {
  background: rgba(255, 255, 255, 0.15);
  color: white;
}

.farm-navbar-dropdown {
  position: relative;
  margin-left: 8px;
}

.farm-navbar-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.15);
  border: none;
  border-radius: var(--farm-radius);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--farm-transition);
}

.farm-navbar-dropdown-trigger:hover {
  background: rgba(255, 255, 255, 0.25);
}

.farm-navbar-dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  min-width: 180px;
  background: white;
  border-radius: var(--farm-radius);
  box-shadow: var(--farm-shadow-xl);
  border: 1px solid var(--farm-border);
  opacity: 0;
  visibility: hidden;
  transform: translateY(-8px);
  transition: var(--farm-transition);
  z-index: 200;
}

.farm-navbar-dropdown:hover .farm-navbar-dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.farm-navbar-dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  color: var(--farm-text-primary);
  text-decoration: none;
  font-size: 14px;
  transition: var(--farm-transition);
  cursor: pointer;
}

.farm-navbar-dropdown-item:first-child {
  border-radius: var(--farm-radius) var(--farm-radius) 0 0;
}

.farm-navbar-dropdown-item:hover {
  background: var(--farm-bg-light);
}

.farm-navbar-dropdown-item-danger:hover {
  background: rgba(239, 68, 68, 0.08);
  color: var(--farm-danger);
}

.farm-navbar-dropdown-divider {
  height: 1px;
  background: var(--farm-border);
  margin: 4px 0;
}
</style>
