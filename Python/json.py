import requests
headers = {
    'Content-Type': 'application/json'
}
requestResponse = requests.get("https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-02&token=15fd6a8cb82c76c6e845ef46f47956c4319ecaac", headers=headers)
print(requestResponse.json())