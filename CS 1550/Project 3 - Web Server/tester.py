import requests

url = "http://127.0.0.1/test1.txt"
url2 = "http://127.0.0.1/test2.txt"
url3 = "http://127.0.0.1/test3.txt"
url4 = "http://127.0.0.1/test4.txt"
url5 = "http://127.0.0.1/test5.txt"
url6 = "http://127.0.0.1/test6.txt"


for i in range(100):
    response = requests.get(url)
    response = requests.get(url2)
    response = requests.get(url3)
    response = requests.get(url4)
    response = requests.get(url5)
    response = requests.get(url6)




