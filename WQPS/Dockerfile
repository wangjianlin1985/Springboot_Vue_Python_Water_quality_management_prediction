# set the base image 
FROM python:3.6
#add project files
ADD WQPS WQPS
ADD predicter predicter 
ADD Model Model
COPY requirements.txt ./
COPY manage.py ./
# Get pip to download and install requirements:
RUN pip install -r requirements.txt
RUN pip install https://github.com/JamesRitchie/scikit-rvm/archive/master.zip
# Expose ports
EXPOSE 8000
# default command to execute    
CMD python manage.py runserver 0.0.0.0:8000