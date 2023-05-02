from django.http import HttpResponse
from django.http import JsonResponse
from django.shortcuts import render, redirect
from . import service


def index(request):
    return HttpResponse("Hello")


def train(request):
    if request.method == 'GET':
        model_id = request.GET.get("id")
        try:
            data = service.train_model(model_id)
            return JsonResponse({'status': 'success', 'data': data})
        except Exception as e:
            return JsonResponse({'status': str(e)})


def predict(request):
    if request.method == 'GET':
        model_id = request.GET.get("id")
        one = float(request.GET.get("one"))
        two = float(request.GET.get("two"))
        three = float(request.GET.get("three"))
        first_month = eval(request.GET.get("startMonth"))
        second_month = (first_month + 1) % 12
        if first_month + 1 == 12:
            second_month = 12
        third_month = (second_month + 1) % 12
        if second_month + 1 == 12:
            third_month = 12
        try:
            print("raw_data----------")
            print([[one, two, three, first_month, second_month, third_month]])
            data = service.predict_next_month(model_id, [[one, two, three, first_month, second_month, third_month]])
            return JsonResponse({'status': 'success', 'data': data})
        except Exception as e:
            print(e)
            return JsonResponse({'status': str(e)})


def waterquality(request):
    if request.method == 'GET':
        obj = request.GET.get('obj', 'nothing')
        month_num = eval(request.GET.get('month_num', '5'))
        if obj == 'nothing':
            return JsonResponse({'status': 'param error'})
        try:
            data = service.get_last_months_data(obj, month_num)
            return JsonResponse({'status': 'success', 'data': data})
        except Exception as e:
            return JsonResponse({'status': str(e)})
    if request.method == 'POST':
        date = request.POST.get('date', 'nothing')
        ph = eval(request.POST.get('PH', '-1'))
        do = eval(request.POST.get('DO', '-1'))
        nh3n = eval(request.POST.get('NH3N', '-1'))
        if date == 'nothing' or ph == -1 or do == -1 or nh3n == -1:
            return JsonResponse({'status': 'param error'})
        try:
            data = {'date': date, 'PH': ph, 'DO': do, 'NH3N': nh3n}
            service.save_one_waterquality(data)
            return JsonResponse({'status': 'success'})
        except Exception as e:
            return JsonResponse({'status': str(e)})


def test(request):
    data = service.get_uploaded_waterquality()
    print(data)
    return render(request, 'test.html', {'data': data})


def delete(request):
    id = request.GET.get('id')
    service.delete(id)
    return redirect('/predicter/test/')







