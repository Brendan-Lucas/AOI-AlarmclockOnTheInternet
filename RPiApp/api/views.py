# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from rest_framework import viewsets
from .models import alarm_structure
from .serializers import AlarmSerializer
# Create your views here.

class alarm_structure_viewset(viewsets.ModelViewSet):
    queryset = alarm_structure.objects.all()
    serializer_class = AlarmSerializer

def home(request):
    return render(request, 'index.html')
