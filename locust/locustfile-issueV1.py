import random

from locust import task, FastHttpUser


class HelloWorld(FastHttpUser):
  connection_timeout = 10.0
  network_timeout = 10.0

  @task
  def issue(self):
    couponId = "2obglswPnFn8RG9lnqFY1Ucl8qg"  # 여기에 쿠폰id 만들어서 넣기
    userId = random.randint(1, 10000000)

    with self.rest("POST", f"/coupons/{couponId}/issue?userId={userId}"):
      pass
