
# Create your models here.
# This is an auto-generated Django Model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each Model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class Waterquality(models.Model):
    id = models.IntegerField(db_column='id', primary_key=True)
    ph = models.FloatField(db_column='PH', blank=True, null=True)  # Field name made lowercase.
    do = models.FloatField(db_column='DO', blank=True, null=True)  # Field name made lowercase.
    nh3n = models.FloatField(db_column='NH3N', blank=True, null=True)  # Field name made lowercase.
    date = models.DateTimeField()
    station = models.IntegerField(blank=True, null=True)

    class Meta:
        # managed = False
        db_table = 'waterquality'


class Model(models.Model):
    id = models.IntegerField(db_column='id', primary_key=True)
    name = models.CharField(max_length=50)
    target = models.CharField(max_length=50)
    date = models.DateTimeField()
    rmse = models.FloatField()

    class Meta:
        db_table = "model"
