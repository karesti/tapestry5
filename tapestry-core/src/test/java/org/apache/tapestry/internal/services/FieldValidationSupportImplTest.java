package org.apache.tapestry.internal.services;

import org.apache.tapestry.*;
import org.apache.tapestry.corelib.internal.InternalMessages;
import org.apache.tapestry.ioc.Messages;
import org.apache.tapestry.runtime.ComponentEventException;
import org.apache.tapestry.services.FieldValidationSupport;
import org.apache.tapestry.services.ValidationMessagesSource;
import org.apache.tapestry.test.TapestryTestCase;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.testng.annotations.Test;

import java.util.Locale;

public class FieldValidationSupportImplTest extends TapestryTestCase
{

    @SuppressWarnings({"unchecked"})
    @Test
    public void parse_client_via_event() throws ValidationException
    {
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        String clientValue = "abracadabra";

        IAnswer answer = new IAnswer()
        {
            @SuppressWarnings({"unchecked"})
            public Object answer() throws Throwable
            {
                Object[] args = EasyMock.getCurrentArguments();
                Object[] context = (Object[]) args[1];
                ComponentEventHandler handler = (ComponentEventHandler) args[2];

                // Pretend that the parser event handler converted it to upper case.

                return handler.handleResult(context[0].toString().toUpperCase(), null, null);
            }
        };

        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.PARSE_CLIENT_EVENT),
                                               EasyMock.isA(Object[].class),
                                               EasyMock.isA(ComponentEventHandler.class))).andAnswer(answer);


        replay();


        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        Object actual = support.parseClient(clientValue, resources, translator);

        assertEquals(actual, clientValue.toUpperCase());

        verify();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test
    public void parse_client_event_handler_throws_validation_exception() throws Exception
    {
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationException ve = new ValidationException("Just didn't feel right.");
        ValidationMessagesSource source = mockValidationMessagesSource();

        String clientValue = "abracadabra";


        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.PARSE_CLIENT_EVENT),
                                               EasyMock.isA(Object[].class),
                                               EasyMock.isA(ComponentEventHandler.class))).andThrow(
                new ComponentEventException(ve.getMessage(), null, ve));


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        try
        {
            support.parseClient(clientValue, resources, translator);

            unreachable();
        }
        catch (ValidationException ex)
        {
            assertSame(ex, ve);
        }


        verify();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test
    public void parse_client_event_handler_fails_with_other_exception() throws Exception
    {
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        RuntimeException re = new RuntimeException("Just didn't feel right.");
        ComponentEventException cee = new ComponentEventException(re.getMessage(), null, re);
        ValidationMessagesSource source = mockValidationMessagesSource();

        String clientValue = "abracadabra";


        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.PARSE_CLIENT_EVENT),
                                               EasyMock.isA(Object[].class),
                                               EasyMock.isA(ComponentEventHandler.class))).andThrow(cee);


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        try
        {
            support.parseClient(clientValue, resources, translator);

            unreachable();
        }
        catch (ComponentEventException ex)
        {
            assertSame(ex, cee);
            assertSame(ex.getCause(), re);
        }


        verify();
    }

    @Test
    public void parse_client_via_translator() throws ValidationException
    {
        Messages messages = mockMessages();
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationMessagesSource source = mockValidationMessagesSource();
        Locale locale = Locale.GERMAN;

        String clientValue = "abracadabra";


        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.PARSE_CLIENT_EVENT),
                                               EasyMock.isA(Object[].class),
                                               EasyMock.isA(ComponentEventHandler.class))).andReturn(false);

        train_getLocale(resources, locale);

        train_getValidationMessages(source, locale, messages);

        expect(translator.parseClient(clientValue, messages)).andReturn("foobar");

        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        Object actual = support.parseClient(clientValue, resources, translator);

        assertEquals(actual, "foobar");

        verify();
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void to_client_via_translator()
    {
        Object value = new Object();
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        String clientValue = "abracadabra";

        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.TO_CLIENT_EVENT),
                                               EasyMock.aryEq(new Object[]{value}),
                                               EasyMock.isA(ComponentEventHandler.class))).andReturn(false);

        expect(translator.toClient(value)).andReturn(clientValue);

        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        String actual = support.toClient(value, resources, translator);

        assertEquals(actual, clientValue);

        verify();
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void to_client_via_event_handler() throws Exception
    {
        Object value = new Object();
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        final String clientValue = "abracadabra";

        IAnswer answer = new IAnswer()
        {
            @SuppressWarnings({"unchecked"})
            public Object answer() throws Throwable
            {
                Object[] args = EasyMock.getCurrentArguments();
                ComponentEventHandler handler = (ComponentEventHandler) args[2];

                return handler.handleResult(clientValue, null, null);
            }
        };

        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.TO_CLIENT_EVENT),
                                               EasyMock.aryEq(new Object[]{value}),
                                               EasyMock.isA(ComponentEventHandler.class))).andAnswer(answer);


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        String actual = support.toClient(value, resources, translator);

        assertEquals(actual, clientValue);

        verify();
    }

    @SuppressWarnings({"unchecked"})
    public void to_client_via_event_handler_returns_non_string() throws Exception
    {
        Object value = new Object();
        ComponentResources resources = mockComponentResources();
        Translator translator = mockTranslator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        IAnswer answer = new IAnswer()
        {
            @SuppressWarnings({"unchecked"})
            public Object answer() throws Throwable
            {
                Object[] args = EasyMock.getCurrentArguments();
                ComponentEventHandler handler = (ComponentEventHandler) args[2];

                // Return an innappropriate value.

                return handler.handleResult(this, null, null);
            }
        };

        EasyMock.expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.TO_CLIENT_EVENT),
                                               EasyMock.aryEq(new Object[]{value}),
                                               EasyMock.isA(ComponentEventHandler.class))).andAnswer(answer);


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        try
        {

            support.toClient(value, resources, translator);

            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertEquals(ex.getMessage(), InternalMessages.toClientShouldReturnString());
        }

        verify();
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void event_triggered_after_delegate_invoked() throws Exception
    {
        getMocksControl().checkOrder(true);

        ComponentResources resources = mockComponentResources();
        FieldValidator fv = mockFieldValidator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        Object value = new Object();

        fv.validate(value);

        ComponentEventHandler handler = null;

        expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.VALIDATE_EVENT),
                                      EasyMock.aryEq(new Object[]{value}), EasyMock.eq(handler))).andReturn(true);


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);

        support.validate(value, resources, fv);

        verify();
    }

    @SuppressWarnings({"unchecked", "ThrowableInstanceNeverThrown"})
    @Test
    public void event_trigger_throws_validation_exception() throws Exception
    {
        ComponentResources resources = mockComponentResources();
        FieldValidator fv = mockFieldValidator();
        ValidationMessagesSource source = mockValidationMessagesSource();

        Object value = new Object();

        ValidationException ve = new ValidationException("Bah!");
        ComponentEventException cee = new ComponentEventException(ve.getMessage(), null, ve);

        ComponentEventHandler handler = null;

        fv.validate(value);

        expect(resources.triggerEvent(EasyMock.eq(FieldValidationSupportImpl.VALIDATE_EVENT),
                                      EasyMock.aryEq(new Object[]{value}), EasyMock.eq(handler))).andThrow(cee);


        replay();

        FieldValidationSupport support = new FieldValidationSupportImpl(source);


        try
        {
            support.validate(value, resources, fv);
            unreachable();
        }
        catch (ValidationException ex)
        {
            assertSame(ex, ve);
        }

        verify();
    }
}