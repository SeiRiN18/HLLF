from django.shortcuts import render, get_object_or_404, redirect
from django.contrib.auth import login, logout, authenticate
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.core.exceptions import PermissionDenied
from .models import Article
from .forms import ArticleForm, RegisterForm


def article_list(request):
    articles = Article.objects.select_related('author').all()
    return render(request, 'articles/article_list.html', {'articles': articles})


def article_detail(request, pk):
    article = get_object_or_404(Article, pk=pk)
    return render(request, 'articles/article_detail.html', {'article': article})


@login_required
def article_create(request):
    if request.method == 'POST':
        form = ArticleForm(request.POST)
        if form.is_valid():
            article = form.save(commit=False)
            article.author = request.user
            article.save()
            messages.success(request, 'Статтю успішно створено!')
            return redirect('article_detail', pk=article.pk)
    else:
        form = ArticleForm()
    return render(request, 'articles/article_form.html', {
        'form': form,
        'title': 'Нова стаття',
    })


@login_required
def article_edit(request, pk):
    article = get_object_or_404(Article, pk=pk)
    if article.author != request.user and not request.user.is_staff:
        raise PermissionDenied
    if request.method == 'POST':
        form = ArticleForm(request.POST, instance=article)
        if form.is_valid():
            form.save()
            messages.success(request, 'Статтю успішно оновлено!')
            return redirect('article_detail', pk=article.pk)
    else:
        form = ArticleForm(instance=article)
    return render(request, 'articles/article_form.html', {
        'form': form,
        'title': 'Редагувати статтю',
        'article': article,
    })


@login_required
def article_delete(request, pk):
    article = get_object_or_404(Article, pk=pk)
    if article.author != request.user and not request.user.is_staff:
        raise PermissionDenied
    if request.method == 'POST':
        article.delete()
        messages.success(request, 'Статтю видалено!')
        return redirect('article_list')
    return render(request, 'articles/article_confirm_delete.html', {'article': article})


def register_view(request):
    if request.user.is_authenticated:
        return redirect('article_list')
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            user = form.save()
            login(request, user)
            messages.success(request, f'Ласкаво просимо, {user.username}!')
            return redirect('article_list')
    else:
        form = RegisterForm()
    return render(request, 'registration/register.html', {'form': form})


def login_view(request):
    if request.user.is_authenticated:
        return redirect('article_list')
    if request.method == 'POST':
        username = request.POST.get('username', '')
        password = request.POST.get('password', '')
        user = authenticate(request, username=username, password=password)
        if user:
            login(request, user)
            next_url = request.GET.get('next', '/')
            return redirect(next_url)
        messages.error(request, "Невірне ім'я користувача або пароль")
    return render(request, 'registration/login.html')


def logout_view(request):
    logout(request)
    return redirect('article_list')
