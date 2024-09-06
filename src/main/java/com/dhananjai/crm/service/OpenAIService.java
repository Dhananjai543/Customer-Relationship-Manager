package com.dhananjai.crm.service;

import com.dhananjai.crm.entity.Answer;
import com.dhananjai.crm.entity.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
