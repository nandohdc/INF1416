f= open("passwords.txt","w+")

for i in range(1000000):
     f.write("%06d\n" % i)

f.close()
