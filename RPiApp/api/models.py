# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.


class alarm_structure(models.Model):
    title = models.CharField(max_length=200)
    body = models.TextField(max_length=200)
    def __unicode__(self):
        return self.title