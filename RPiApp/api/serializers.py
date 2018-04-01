from rest_framework import serializers
from .models import alarm_structure

class AlarmSerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model=alarm_structure
        fields=('title', 'body')