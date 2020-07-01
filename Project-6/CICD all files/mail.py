import smtplib

sender_email = "vedantshrivastava466@gmail.com"
rec_email = "github@gmail.com"
password = "********"
message = "Hello Developer, your website has some error......plz check the code. and push again"

server = smtplib.SMTP('smtp.gmail.com', 587)
server.starttls()
server.login(sender_email, password)
print("Login success")
server.sendmail(sender_email, rec_email, message)
print("Email has been sent to ", rec_email)
