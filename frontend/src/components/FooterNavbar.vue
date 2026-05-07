<template>
  <footer class="farm-footer">
    <div class="farm-footer-container">
      <div class="farm-footer-brand">
        <svg
          width="24"
          height="24"
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
        <span>西塘草舍农场</span>
      </div>
      <div class="farm-footer-text">
        2020 - 2026 西塘草舍农场 · 绿色农业 · 健康生活
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "FooterNavbar"
};
</script>

<style scoped>
.farm-footer {
  background: linear-gradient(
    135deg,
    var(--farm-primary-dark) 0%,
    var(--farm-primary) 100%
  );
  padding: 24px 0;
  margin-top: auto;
}

.farm-footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.farm-footer-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  font-size: 16px;
  font-weight: 600;
}

.farm-footer-text {
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
}
</style>
