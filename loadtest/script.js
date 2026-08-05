import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errors = new Rate('business_errors');
const BASE = __ENV.BASE_URL || 'http://localhost:8085';

export const options = {
  stages: [
    { duration: '1m', target: 30 },
    { duration: '2m', target: 100 },
    { duration: '5m', target: 170 },   // ~10,200 req/min
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1500'],
    http_req_failed: ['rate<0.01'],
    business_errors: ['rate<0.01'],
  },
};

export function setup() {
  const email = `load_${Date.now()}@example.com`;
  const password = 'loadtest-password-123';

  const res = http.post(`${BASE}/api/v1/auth/register`, JSON.stringify({
    username: `load_${Date.now()}`,
    email, phone: `05${Math.floor(Math.random() * 100000000)}`,
    address: 'Riyadh', password,
  }), { headers: { 'Content-Type': 'application/json' } });

  if (res.status !== 201) throw new Error(`setup failed: ${res.status} ${res.body}`);

  return { token: res.json('token'), email, password };
}

export default function (data) {
  const authed = {
    headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${data.token}` },
  };

  // 70% — anonymous browsing
  group('browse', () => {
    const page = Math.floor(Math.random() * 50);
    const res = http.get(`${BASE}/api/v1/books/allbooks?page=${page}&size=20`);
    errors.add(!check(res, { 'list 200': (r) => r.status === 200 }));
  });

  // 20% — authenticated cart reads
  if (Math.random() < 0.2) {
    group('cart', () => {
      const res = http.get(`${BASE}/api/v1/cart`, authed);
      errors.add(!check(res, { 'cart 200': (r) => r.status === 200 }));
    });
  }

  // 5% — writes
  if (Math.random() < 0.05) {
    group('add-to-cart', () => {
      const bookId = Math.floor(Math.random() * 1000) + 1;
      const res = http.post(`${BASE}/api/v1/cart/items`,
        JSON.stringify({ bookId, quantity: 1 }), authed);
      errors.add(!check(res, { 'add ok': (r) => r.status === 200 || r.status === 400 }));
    });
  }

  sleep(Math.random() * 2);
}