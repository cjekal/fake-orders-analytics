from faker import Faker
import datetime
import csv

def jitter_value(faker, date, baseline_value, adj_factor):
    days_since_epoch = (date - datetime.date(2017, 1, 3)).days
    return baseline_value + (faker.random_int(min=-5000, max=5000) / 100) + ((1 + adj_factor) ** days_since_epoch)

if __name__ == "__main__":
    faker = Faker()
    data = []

    for _ in range(10000):
        order_date = faker.date_between_dates(date_start=datetime.date(2017, 1, 3), date_end=datetime.date(2021, 4, 28))
        order = {
            "order_date": order_date,
            "order_qty": faker.random_int(min=100, max=5000),
            "customer_id": faker.random_int(min=1, max=3),
            "product_category_id": faker.random_int(min=1, max=3),
            "cost_per_unit": jitter_value(faker, order_date, 500, 0.00005),
            "price_per_unit": jitter_value(faker, order_date, 600, 0.00005)
        }
        data.append(order)
    
    with open("data/spark/orders_history_temp.csv", "w") as f:
        dict_writer = csv.DictWriter(f, fieldnames=["order_date", "order_qty", "customer_id", "product_category_id", "cost_per_unit", "price_per_unit"])
        dict_writer.writeheader()
        dict_writer.writerows(data)
