<template>
  <div class="toast-container">
    <div
      v-for="toast in toasts"
      :key="toast.id"
      :class="['toast', `toast-${toast.type}`, toast.fadeOut ? 'toast-fade-out' : '']"
    >
      <div class="toast-icon">
        <svg v-if="toast.type === 'success'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
          <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
        <svg v-else-if="toast.type === 'error'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"></circle>
          <line x1="15" y1="9" x2="9" y2="15"></line>
          <line x1="9" y1="9" x2="15" y2="15"></line>
        </svg>
        <svg v-else-if="toast.type === 'warning'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
          <line x1="12" y1="9" x2="12" y2="13"></line>
          <line x1="12" y1="17" x2="12.01" y2="17"></line>
        </svg>
        <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"></circle>
          <line x1="12" y1="16" x2="12" y2="12"></line>
          <line x1="12" y1="8" x2="12.01" y2="8"></line>
        </svg>
      </div>
      <div class="toast-content">{{ toast.message }}</div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";

export default {
  data() {
    return {
      toasts: [],
      lastErrorTime: 0,
      errorDebounceTime: 1000,
      toastDuration: 3000
    };
  },
  created() {
    bus.$on(bus.error, (data) => {
      const now = Date.now();
      if (now - this.lastErrorTime > this.errorDebounceTime) {
        this.lastErrorTime = now;
        this.showToast(data, "error");
      }
    });
    bus.$on("toast", (data) => {
      this.showToast(data.message, data.type || "info");
    });
  },
  beforeDestroy() {
    bus.$off(bus.error);
    bus.$off("toast");
  },
  methods: {
    showToast(message, type = "info") {
      const toast = {
        id: Date.now() + Math.random(),
        message,
        type,
        fadeOut: false
      };

      this.toasts.push(toast);

      setTimeout(() => {
        toast.fadeOut = true;
        setTimeout(() => {
          this.removeToast(toast);
        }, 300);
      }, this.toastDuration);
    },
    removeToast(toast) {
      const index = this.toasts.findIndex(t => t.id === toast.id);
      if (index !== -1) {
        this.toasts.splice(index, 1);
      }
    }
  }
};
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 100px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
  pointer-events: none;
}

.toast {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  border-radius: var(--farm-radius);
  box-shadow: var(--farm-shadow-lg);
  min-width: 200px;
  max-width: 400px;
  animation: toast-in 0.3s ease-out forwards;
  pointer-events: auto;
}

.toast-fade-out {
  animation: toast-out 0.3s ease-in forwards;
}

.toast-icon {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
}

.toast-content {
  flex: 1;
  font-size: 13px;
  font-weight: 500;
  line-height: 1.4;
  text-align: center;
}

.toast-success {
  background: linear-gradient(135deg, var(--farm-success), #34d399);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.toast-error {
  background: linear-gradient(135deg, var(--farm-danger), #f87171);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.toast-warning {
  background: linear-gradient(135deg, var(--farm-warning), #fbbf24);
  color: #b45309;
  border: 1px solid rgba(180, 83, 9, 0.2);
}

.toast-info {
  background: linear-gradient(135deg, var(--farm-info), #22d3ee);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

@keyframes toast-in {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes toast-out {
  from {
    opacity: 1;
    transform: translateY(0);
  }
  to {
    opacity: 0;
    transform: translateY(-20px);
  }
}
</style>