f= open("passwords.txt","w+")

for i in range(1000000):
     f.write("%06d\n" % i)
for i in range(1000000):
     f.write("%07d\n" % i)
for i in range(1000000):
     f.write("%08d\n" % i)
f.close()
